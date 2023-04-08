package org.jd.benoggl.common.events

import org.jd.benoggl.common.copyOf
import org.jd.benoggl.common.models.*
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class BidPassedEventTest {

    @Test
    fun apply_validPass_updatesBidStateOfGame() {
        val player1 = Player("player1", "player1")
        val player2 = Player("player2", "player2")
        val player3 = Player("player3", "player3")
        val game = Game(players = mutableListOf(player1, player2, player3))
        game.rounds += Round(1, RoundState.BIDDING)
        game.bidding = Bidding(game.players.copyOf())
        game.bidding.bids += Bid(150, player1)

        val sut = BidPassedEvent(player2.uid)

        sut.apply(game)

        assertContentEquals(listOf(player1, player3), game.bidding.remainingPlayers)
    }

    @Test
    fun apply_invalidPassNotByFirstTwoPlayers_throwsException() {
        val player1 = Player("player1", "player1")
        val player2 = Player("player2", "player2")
        val player3 = Player("player3", "player3")
        val game = Game(players = mutableListOf(player1, player2, player3))
        game.rounds += Round(1, RoundState.BIDDING)
        game.bidding = Bidding(game.players.copyOf())
        game.bidding.bids += Bid(150, player1)

        val sut = BidPassedEvent(player3.uid)

        assertFailsWith<IllegalEventException> {
            sut.apply(game)
        }

        assertContentEquals(listOf(player1, player2, player3), game.bidding.remainingPlayers)
    }

    @Test
    fun apply_invalidPassByHighestBidder_throwsException() {
        val player1 = Player("player1", "player1")
        val player2 = Player("player2", "player2")
        val player3 = Player("player3", "player3")
        val game = Game(players = mutableListOf(player1, player2, player3))
        game.rounds += Round(1, RoundState.BIDDING)
        game.bidding = Bidding(game.players.copyOf())
        game.bidding.bids += Bid(150, player1)

        val sut = BidPassedEvent(player1.uid)

        assertFailsWith<IllegalEventException> {
            sut.apply(game)
        }

        assertContentEquals(listOf(player1, player2, player3), game.bidding.remainingPlayers)
    }

    @Test
    fun apply_invalidPassNoBidPlacedYet_throwsException() {
        val player1 = Player("player1", "player1")
        val player2 = Player("player2", "player2")
        val player3 = Player("player3", "player3")
        val game = Game(players = mutableListOf(player1, player2, player3))
        game.rounds += Round(1, RoundState.BIDDING)
        game.bidding = Bidding(game.players.copyOf())

        val sut = BidPassedEvent(player1.uid)

        assertFailsWith<IllegalEventException> {
            sut.apply(game)
        }

        assertContentEquals(listOf(player1, player2, player3), game.bidding.remainingPlayers)
    }

    @Test
    fun createChildEvents_onePlayerRemains_assignsDabAndStartsMelding() {
        val player1 = Player("player1", "player1")
        val player2 = Player("player2", "player2")
        val player3 = Player("player3", "player3")
        val game = Game(players = mutableListOf(player1, player2, player3))
        game.rounds += Round(1, RoundState.BIDDING)
        game.bidding = Bidding(mutableListOf(player1))
        game.bidding.bids += Bid(150, player1)

        val sut = BidPassedEvent(player1.uid)

        val result = sut.createChildEvents(game)

        assertTrue(result.contains(DabbAssignedEvent(player1.uid)))
        assertTrue(result.contains(MeldingStartedEvent))
    }

    @Test
    fun createChildEvents_multiplePlayersRemain_doesNothing() {
        val player1 = Player("player1", "player1")
        val player2 = Player("player2", "player2")
        val player3 = Player("player3", "player3")
        val game = Game(players = mutableListOf(player1, player2, player3))
        game.rounds += Round(1, RoundState.BIDDING)
        game.bidding = Bidding(mutableListOf(player1, player3))
        game.bidding.bids += Bid(150, player1)

        val sut = BidPassedEvent(player1.uid)

        val result = sut.createChildEvents(game)

        assertTrue(result.isEmpty())
    }

}