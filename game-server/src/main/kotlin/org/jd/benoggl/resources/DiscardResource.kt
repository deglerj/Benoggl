package org.jd.benoggl.resources

import org.jd.benoggl.entities.BiddingEntity
import org.jd.benoggl.entities.PlayerHandEntity
import org.jd.benoggl.entities.RoundEntity
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.models.Card
import org.jd.benoggl.models.RoundState
import org.jd.benoggl.removeFirst
import org.jd.benoggl.resources.dtos.CardDto
import org.jd.benoggl.resources.dtos.DiscardDto
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Validator
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/game/{gameUid}/round/{roundNumber}/discard")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
class DiscardResource {

    @Inject
    @field: Default
    internal lateinit var validator: Validator

    @POST
    fun updateDiscard(
        @PathParam("gameUid") gameUid: String,
        @PathParam("roundNumber") roundNumber: Int,
        discard: DiscardDto
    ) {
        validator.validateRestDto(discard)

        if (isUnknownRound(gameUid, roundNumber)) {
            throw BadRequestException("Round $roundNumber of game $gameUid does not exit")
        }

        if (isRoundInWrongState(gameUid, roundNumber)) {
            throw BadRequestException("Discarding is currently not allowed in round $roundNumber of game $gameUid")
        }

        if (isDiscardByWrongPlayer(gameUid, roundNumber, discard.playerUid)) {
            throw BadRequestException("Player ${discard.playerUid} is not allowed to discard cards in round $roundNumber of game $gameUid")
        }

        if (isDiscardWithWrongAmountOfCards(discard.cards!!)) {
            throw BadRequestException("Discard contains too many or too few cards (expected: 4, actual: ${discard.cards.size}")
        }

        discardCards(gameUid, roundNumber, discard.playerUid!!, discard.cards.map(CardDto::toModel))

        finishDiscarding(gameUid, roundNumber)
    }

    private fun isUnknownRound(gameUid: String, roundNumber: Int) =
        RoundEntity.findByNumber(roundNumber, gameUid) == null

    private fun isRoundInWrongState(gameUid: String, roundNumber: Int) =
        RoundEntity.findByNumber(roundNumber, gameUid)?.state != RoundState.DISCARDING

    private fun isDiscardByWrongPlayer(gameUid: String, roundNumber: Int, playerUid: String?) =
        BiddingEntity.findByNumber(roundNumber, gameUid)?.highestBidder?.uid != playerUid

    private fun isDiscardWithWrongAmountOfCards(cards: Collection<CardDto>): Boolean = cards.size != 4

    private fun discardCards(gameUid: String, roundNumber: Int, playerUid: String, cardsToDiscard: List<Card>) {
        val playerHand = PlayerHandEntity.findPlayerHand(playerUid, gameUid, roundNumber)
        val discard = RoundEntity.findByNumber(roundNumber, gameUid)!!.discard

        // Remove cards to discard from player hand
        cardsToDiscard.forEach { cardToDiscard ->
            val discardedCard = playerHand.hand.cards.removeFirst { handCard -> handCard.toModel() == cardToDiscard }
                ?: throw BadRequestException("Could not discard card ${cardToDiscard}, it's not on the player's hand")
            discardedCard.hand = discard
            discard.cards.add(discardedCard)
            discardedCard.persist()
        }

        discard.persist()
        playerHand.persist()
    }

    private fun finishDiscarding(gameUid: String, roundNumber: Int) {
        val round = RoundEntity.findByNumber(roundNumber, gameUid)!!
        round.state = RoundState.TRICKING
        round.persist()
    }

}