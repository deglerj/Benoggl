package org.jd.benoggl.common.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jd.benoggl.common.models.Bid
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Player
import org.jd.benoggl.common.models.findByUid

@Serializable
@SerialName("bid-placed")
data class BidPlacedEvent(val points: Int, val playerUid: String) : Event {

    override fun apply(game: Game) {
        mustBeNewHighestBid(game)
        mustBeByTwoFirstBiddingPlayers(game)

        val player = game.players.findByUid(playerUid)
        game.bidding.bids.add(Bid(points, player))
    }

    private fun mustBeNewHighestBid(game: Game) {
        val highestBid = game.bidding.highestBid?.points ?: 0
        if (points <= highestBid) {
            throw IllegalEventException("Player $playerUid placed bid over $points points, but highest bid is already $highestBid")
        }
    }

    private fun mustBeByTwoFirstBiddingPlayers(game: Game) {
        val biddingPlayer1Uid = game.bidding.remainingPlayers.getOrNull(0)?.uid
        val biddingPlayer2Uid = game.bidding.remainingPlayers.getOrNull(1)?.uid

        if (playerUid != biddingPlayer1Uid && playerUid != biddingPlayer2Uid) {
            throw IllegalEventException("Player $playerUid is currently not allowed to place bids")
        }
    }

    override fun isVisibleForPlayer(game: Game, player: Player) = false

    override fun explain(game: Game) = "Bid over $points points placed by player $playerUid"

}