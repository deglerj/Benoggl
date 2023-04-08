package org.jd.benoggl.common.events

import org.jd.benoggl.common.copyOf
import org.jd.benoggl.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

internal class BidPlacedEventTest {

    @Test
    fun apply_validBid_updatesBidStateOfGame() {
        val player1 = Player("player1", "player1")
        val player2 = Player("player2", "player2")
        val player3 = Player("player3", "player3")
        val game = Game(players = mutableListOf(player1, player2, player3))
        game.rounds += Round(1, RoundState.BIDDING)
        game.bidding = Bidding(game.players.copyOf())

        val sut = BidPlacedEvent(150, player1.uid)

        sut.apply(game)

        assertEquals(150, game.bidding.highestBid?.points)
        assertEquals(player1, game.bidding.highestBid?.player)
    }

    @Test
    fun apply_invalidBidNotByFirstTwoPlayers_throwsException() {
        val player1 = Player("player1", "player1")
        val player2 = Player("player2", "player2")
        val player3 = Player("player3", "player3")
        val game = Game(players = mutableListOf(player1, player2, player3))
        game.rounds += Round(1, RoundState.BIDDING)
        game.bidding = Bidding(game.players.copyOf())

        val sut = BidPlacedEvent(150, player3.uid)

        assertFailsWith<IllegalEventException> {
            sut.apply(game)
        }

        assertNull(game.bidding.highestBid)
    }

    @Test
    fun apply_invalidBidLowerThanHighestBid_throwsException() {
        val player1 = Player("player1", "player1")
        val player2 = Player("player2", "player2")
        val player3 = Player("player3", "player3")
        val game = Game(players = mutableListOf(player1, player2, player3))
        game.rounds += Round(1, RoundState.BIDDING)
        game.bidding = Bidding(game.players.copyOf())
        game.bidding.bids += Bid(200, player1)

        val sut = BidPlacedEvent(150, player2.uid)

        assertFailsWith<IllegalEventException> {
            sut.apply(game)
        }

        assertEquals(1, game.bidding.bids.size)
        assertEquals(200, game.bidding.highestBid?.points)
        assertEquals(player1, game.bidding.highestBid?.player)
    }

    @Test
    fun apply_validBidForRoundNotInBiddingState_throwsException() {
        val player1 = Player("player1", "player1")
        val player2 = Player("player2", "player2")
        val player3 = Player("player3", "player3")
        val game = Game(players = mutableListOf(player1, player2, player3))
        game.rounds += Round(1, RoundState.LOST)
        game.bidding = Bidding(game.players.copyOf())

        val sut = BidPlacedEvent(150, player1.uid)

        assertFailsWith<IllegalEventException> {
            sut.apply(game)
        }

        assertNull(game.bidding.highestBid)
    }

    @Test
    fun apply_invalidFirstBidOfZero_throwsException() = apply_invalidFirstBid_throwsException(0)

    @Test
    fun apply_invalidFirstNegativeBid_throwsException() = apply_invalidFirstBid_throwsException(-100)

    private fun apply_invalidFirstBid_throwsException(points: Int) {
        val player1 = Player("player1", "player1")
        val player2 = Player("player2", "player2")
        val player3 = Player("player3", "player3")
        val game = Game(players = mutableListOf(player1, player2, player3))
        game.rounds += Round(1, RoundState.BIDDING)
        game.bidding = Bidding(game.players.copyOf())

        val sut = BidPlacedEvent(points, player1.uid)

        assertFailsWith<IllegalEventException> {
            sut.apply(game)
        }

        assertNull(game.bidding.highestBid)
    }

}