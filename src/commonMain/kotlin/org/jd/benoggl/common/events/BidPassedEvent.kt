package org.jd.benoggl.common.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jd.benoggl.common.models.*

@Serializable
@SerialName("bid-passed")
data class BidPassedEvent(val playerUid: String) : Event {

    override fun apply(game: Game) {
        mustHaveBiddingStateForCurrentRound(game)
        mustBeByTwoFirstBiddingPlayers(game)
        mustNotBeByHighestBidder(game)
        mustNotBeEmptyBidding(game)

        val player = game.players.findByUid(playerUid)
        game.bidding.remainingPlayers -= player
    }

    private fun mustHaveBiddingStateForCurrentRound(game: Game) {
        val state = game.currentRound.state
        if (state != RoundState.BIDDING) {
            throw IllegalEventException("Current round must be in bidding state, but actual state is $state")
        }
    }

    private fun mustBeByTwoFirstBiddingPlayers(game: Game) {
        val biddingPlayer1Uid = game.bidding.remainingPlayers.getOrNull(0)?.uid
        val biddingPlayer2Uid = game.bidding.remainingPlayers.getOrNull(1)?.uid

        if (playerUid != biddingPlayer1Uid && playerUid != biddingPlayer2Uid) {
            throw IllegalEventException("Player $playerUid is currently not allowed to pass bidding")
        }
    }

    private fun mustNotBeByHighestBidder(game: Game) {
        val highestBidder = game.bidding.highestBid?.player

        if (highestBidder?.uid === playerUid) {
            throw IllegalEventException("Player $playerUid is the highest bidder and must not pass bidding")
        }
    }

    private fun mustNotBeEmptyBidding(game: Game) {
        if (game.bidding.bids.isEmpty()) {
            throw IllegalEventException("No bids have been placed yet, player $playerUid must not pass bidding")
        }
    }

    override fun createChildEvents(game: Game): List<Event> {
        return if (game.bidding.remainingPlayers.size == 1) {
            val lastPlayer = game.bidding.remainingPlayers.first()

            // Sanity check
            if (lastPlayer !== game.bidding.highestBid?.player) {
                throw IllegalStateException("Last remaining player in bidding (${lastPlayer.uid}) is not the highest bidder (${game.bidding.highestBid?.player?.uid})")
            }

            listOf(
                DabbAssignedEvent(lastPlayer.uid),
                MeldingStartedEvent
            )
        } else {
            listOf()
        }
    }

    override fun isVisibleForPlayer(game: Game, player: Player) = true

    override fun explain(game: Game) = "Player $playerUid passed bidding"

}