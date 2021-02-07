package org.jd.benoggl.resources

import org.jd.benoggl.entities.*
import org.jd.benoggl.mappers.toDto
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.models.MeldCombination
import org.jd.benoggl.models.RoundState
import org.jd.benoggl.resources.dtos.CardDto
import org.jd.benoggl.resources.dtos.MeldDto
import org.jd.benoggl.rules.meldcombinations.MeldCombinationsInCardsFinder
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Validator
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/game/{gameUid}/round/{roundNumber}/meld")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
class MeldResource {

    @Inject
    @field: Default
    internal lateinit var validator: Validator

    @GET
    fun getMelds(@PathParam("gameUid") gameUid: String, @PathParam("roundNumber") roundNumber: Int): List<MeldDto> {
        val trump = RoundEntity.findByNumber(roundNumber, gameUid)?.trump
        return MeldEntity.findForRound(roundNumber, gameUid)
            .map(MeldEntity::toModel)
            .map { it.toDto(trump) }
    }


    @PUT
    fun addMeld(
        @PathParam("gameUid") gameUid: String,
        @PathParam("roundNumber") roundNumber: Int,
        meld: MeldDto
    ) {
        validator.validateRestDto(meld)

        if (isMeldingForRoundNotActive(gameUid, roundNumber)) {
            throw BadRequestException("Melding is currently not active for round $roundNumber of game $gameUid")
        }

        if (isMeldAlreadyPresentForPlayer(gameUid, roundNumber, meld.playerUid!!)) {
            throw BadRequestException("Player ${meld.playerUid} has already submitted a meld in round $roundNumber of game $gameUid")
        }

        when {
            isInitialMeldByPlayerWithBid(gameUid, roundNumber, meld.playerUid) -> {
                persistTrumpChoice(gameUid, roundNumber, meld)
            }
            containsInvalidTrumpChoice(gameUid, roundNumber, meld) -> {
                throw BadRequestException("Trump can only be set by the player with highest bid")
            }
            isTrumpNotSetYet(gameUid, roundNumber) -> {
                throw BadRequestException("No trump has been set yet for round $roundNumber of game $gameUid")
            }
        }

        if (isMeldTooHigh(gameUid, roundNumber, meld.cards!!, meld.points!!)) {
            throw BadRequestException("Meld is higher than valid for the provided cards.")
        }

        val cardEntities = tryFindingCards(gameUid, roundNumber, meld.playerUid, meld.cards)
        if (cardEntities.size < meld.cards.size) {
            throw BadRequestException("Meld contains cards that are not part of the player's hand.")
        }

        val entity = createEntity(meld, cardEntities, gameUid, roundNumber)
        entity.persist()
    }


    private fun isInitialMeldByPlayerWithBid(gameUid: String, roundNumber: Int, playerUid: String) =
        RoundEntity.findByNumber(roundNumber, gameUid)
            ?.bidding
            ?.highestBidder
            ?.uid == playerUid

    private fun persistTrumpChoice(gameUid: String, roundNumber: Int, meld: MeldDto) {
        val round = RoundEntity.findByNumber(roundNumber, gameUid)!!

        // Trump already set and meld contains different trump choice? → Error
        if (round.trump != null && round.trump != meld.trump) {
            throw BadRequestException("Trump has already been set to ${round.trump}")
        }
        // Trump in DTO is empty? → Error
        else if (meld.trump == null) {
            throw BadRequestException("Trump choice must not be empty for meld by player with highest bid")
        }

        round.trump = meld.trump
        round.persist()
    }

    private fun containsInvalidTrumpChoice(gameUid: String, roundNumber: Int, meld: MeldDto): Boolean {
        if (meld.trump == null) {
            return false
        }

        val round = RoundEntity.findByNumber(roundNumber, gameUid)
        return round?.trump != null && round.trump != meld.trump
    }


    private fun isTrumpNotSetYet(gameUid: String, roundNumber: Int): Boolean =
        RoundEntity.findByNumber(roundNumber, gameUid)?.trump == null

    private fun isMeldingForRoundNotActive(gameUid: String, roundNumber: Int) =
        RoundEntity.findByNumber(roundNumber, gameUid)?.state != RoundState.MELDING

    private fun isMeldAlreadyPresentForPlayer(gameUid: String, roundNumber: Int, playerUid: String) =
        MeldEntity.findForPlayer(playerUid, roundNumber, gameUid) != null

    private fun isMeldTooHigh(gameUid: String, roundNumber: Int, cardDtos: Collection<CardDto>, points: Int): Boolean {
        val cards = cardDtos.map(CardDto::toModel)
        val trump = RoundEntity.findByNumber(roundNumber, gameUid)!!.trump!!
        val maxPoints = MeldCombinationsInCardsFinder().findCombinations(cards, trump)
            .map(MeldCombination::points)
            .sum()

        return points > maxPoints
    }

    private fun tryFindingCards(
        gameUid: String,
        roundNumber: Int,
        playerUid: String,
        meldCards: Collection<CardDto>
    ): Collection<CardEntity> {
        val hand = HandEntity.findForPlayer(playerUid, roundNumber, gameUid)
        val remainingHandCards = ArrayList(hand.cards)
        return meldCards
            .mapNotNull { meldCard ->
                val card = tryFindingCard(meldCard, remainingHandCards)
                remainingHandCards.remove(card)
                return@mapNotNull card
            }
    }

    private fun tryFindingCard(card: CardDto, cards: Collection<CardEntity>) =
        cards
            .filter { it.rank == card.rank }
            .firstOrNull { it.suit == card.suit }

    private fun createEntity(
        meld: MeldDto,
        cards: Collection<CardEntity>,
        gameUid: String,
        roundNumber: Int
    ): MeldEntity {
        val entity = MeldEntity()
        entity.player = PlayerEntity.findByUid(meld.playerUid!!, gameUid)!!
        entity.round = RoundEntity.findByNumber(roundNumber, gameUid)!!
        entity.points = meld.points!!
        entity.cards = cards.toMutableList()
        return entity
    }

}

