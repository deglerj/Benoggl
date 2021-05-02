package org.jd.benoggl.services

import org.jd.benoggl.entities.*
import org.jd.benoggl.models.BiddingState
import org.jd.benoggl.models.Player
import org.jd.benoggl.models.RoundState
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional

@ApplicationScoped
@Transactional
class BidService {

    companion object {
        const val MIN_BID_DISTANCE = 10
    }

    fun isCurrentChallenger(roundNumber: Int, gameUid: String, player: Player) =
        findCurrentChallengerUid(roundNumber, gameUid) == player.uid

    fun removeChallenger(roundNumber: Int, gameUid: String, player: Player, persist: Boolean = true) {
        val bidding = findBidding(roundNumber, gameUid)!!

        if (bidding.challengers[0].uid != player.uid) {
            throw IllegalArgumentException("Player ${player.uid} is not the current challenger for round $roundNumber of game $gameUid")
        }

        bidding.challengers.removeAt(0)

        if (bidding.challengers.isEmpty()) {
            finishBidding(bidding)
        }
    }

    private fun finishBidding(bidding: BiddingEntity) {
        bidding.state = BiddingState.FINISHED

        val round = bidding.round
        round.state = RoundState.MELDING

        assignDabbToHighestBidder(bidding, round)
    }

    private fun assignDabbToHighestBidder(
        bidding: BiddingEntity,
        round: RoundEntity
    ) {
        val highestBidderHand =
            PlayerHandEntity.findPlayerHand(bidding.highestBidder!!.uid, round.game.uid, round.number).hand
        round.dabb.cards.forEach { card ->
            highestBidderHand.cards.add(card)
            card.hand = highestBidderHand
            card.persist()
        }
        highestBidderHand.persist()
        round.dabb.cards.clear()
        round.dabb.persist()
    }


    fun isValidBid(roundNumber: Int, gameUid: String, player: Player, points: Int): Boolean {
        if (!isCurrentChallenger(roundNumber, gameUid, player)) {
            return false
        }

        val highestBid = findBidding(roundNumber, gameUid)!!.highestBid ?: 0
        return points >= highestBid + MIN_BID_DISTANCE
    }

    fun placeBid(roundNumber: Int, gameUid: String, player: Player, points: Int) {
        if (!isValidBid(roundNumber, gameUid, player, points)) {
            throw RuntimeException("Bid $points by player ${player.uid} for round $roundNumber of game $gameUid is not valid")
        }

        val playerEntity = PlayerEntity.findByUid(player.uid, gameUid)!!

        val bidding = findBidding(roundNumber, gameUid)!!
        if (isFirstBid(bidding)) {
            removeChallenger(roundNumber, gameUid, player, false)
            bidding.highestBidder = playerEntity
        } else {
            swapHighestBidderAndCurrentChallenger(bidding)
        }
        bidding.highestBid = points

        val bid = BidEntity()
        bid.player = playerEntity
        bid.points = points
        bid.bidding = bidding
        bidding.bids.add(bid)

        bidding.persist()
    }

    private fun swapHighestBidderAndCurrentChallenger(bidding: BiddingEntity) {
        val newHighestBidder = bidding.challengers[0]
        bidding.challengers[0] = bidding.highestBidder!!
        bidding.highestBidder = newHighestBidder
    }

    private fun isFirstBid(bidding: BiddingEntity) =
        bidding.highestBidder == null

    private fun findCurrentChallengerUid(roundNumber: Int, gameUid: String): String? =
        findBidding(roundNumber, gameUid)?.challengers?.first()?.uid

    private fun findBidding(roundNumber: Int, gameUid: String): BiddingEntity? {
        val round = RoundEntity.findByNumber(roundNumber, gameUid)
        if (round == null || round.state != RoundState.BIDDING) {
            return null
        }
        return round.bidding
    }
}