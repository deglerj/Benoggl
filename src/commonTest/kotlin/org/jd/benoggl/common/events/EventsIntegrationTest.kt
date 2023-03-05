package org.jd.benoggl.common.events

import org.jd.benoggl.common.GameState
import org.jd.benoggl.common.models.*
import org.jd.benoggl.common.models.Rank.*
import org.jd.benoggl.common.models.Suit.*
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class EventsIntegrationTest {

    @Test
    fun simpleGameWithOneRound() {
        // New game
        val game = Game()
        assertEquals(game.state, GameState.RUNNING)
        assertEquals(game.rounds.size, 0)

        // Add three players
        game apply PlayerAddedEvent("player1", "player1")
        game apply PlayerAddedEvent("player2", "player2")
        game apply PlayerAddedEvent("player3", "player3")
        assertEquals(game.players.size, 3)
        assertContentEquals(game.players.map { it.name }, listOf("player1", "player2", "player3"))
        val player1 = game.players.findByUid("player1")
        val player2 = game.players.findByUid("player2")
        val player3 = game.players.findByUid("player3")

        // Start round
        game apply RoundStartedEvent
        assertEquals(game.rounds.size, 1)
        game.players.forEach { player ->
            assertEquals(player.hand.cards.size, 12)
        }
        assertEquals(game.dabb.cards.size, 4)
        assertEquals(game.currentRound.state, RoundState.BIDDING)

        // Overwrite dabb and player hands
        game.dabb = Dabb(
            listOf(
                Card(LEAVES, KING),
                Card(HEARTS, KING),
                Card(HEARTS, UNTER),
                Card(BELLS, TEN),
            )
        )
        // Player 1 → can meld 4-ober and 3 pairs (1 acorn, 2 bells)
        player1.hand = Hand(
            mutableListOf(
                Card(LEAVES, ACE),
                Card(LEAVES, KING),
                Card(LEAVES, OBER),
                Card(HEARTS, TEN),
                Card(HEARTS, OBER),
                Card(HEARTS, OBER),
                Card(BELLS, ACE),
                Card(BELLS, KING),
                Card(BELLS, KING),
                Card(BELLS, OBER),
                Card(BELLS, OBER),
                Card(ACORNS, OBER),
            )
        )
        // Player 2 → can meld nothing
        player2.hand = Hand(
            mutableListOf(
                Card(LEAVES, TEN),
                Card(HEARTS, ACE),
                Card(HEARTS, ACE),
                Card(HEARTS, TEN),
                Card(BELLS, ACE),
                Card(BELLS, UNTER),
                Card(ACORNS, ACE),
                Card(ACORNS, ACE),
                Card(ACORNS, TEN),
                Card(ACORNS, KING),
                Card(ACORNS, UNTER),
                Card(ACORNS, UNTER),
            )
        )
        // Player 3 → can meld acorn family (after winning dabb) + binokel
        player3.hand = Hand(
            mutableListOf(
                Card(LEAVES, ACE),
                Card(LEAVES, TEN),
                Card(LEAVES, OBER),
                Card(LEAVES, UNTER),
                Card(LEAVES, UNTER),
                Card(BELLS, TEN),
                Card(BELLS, UNTER),
                Card(ACORNS, TEN),
                Card(ACORNS, KING),
                Card(ACORNS, OBER),
                Card(HEARTS, KING),
                Card(HEARTS, UNTER),
            )
        )

        // Place bids, player 3 wins with 260
        game apply BidPlacedEvent(150, player1.uid)
        game apply BidPlacedEvent(160, player2.uid)
        game apply BidPlacedEvent(170, player1.uid)
        game apply BidPlacedEvent(180, player2.uid)
        game apply BidPlacedEvent(190, player1.uid)
        game apply BidPassedEvent(player2.uid)
        game apply BidPlacedEvent(200, player3.uid)
        game apply BidPlacedEvent(210, player1.uid)
        game apply BidPlacedEvent(220, player3.uid)
        game apply BidPlacedEvent(230, player1.uid)
        game apply BidPlacedEvent(240, player3.uid)
        game apply BidPlacedEvent(250, player1.uid)
        game apply BidPlacedEvent(260, player3.uid)
        game apply BidPassedEvent(player1.uid)
        assertEquals(game.currentRound.state, RoundState.MELDING)
        assertContentEquals(game.bidding.remainingPlayers, listOf(player3))
        assertEquals(game.bidding.highestBid?.player, player3)
        assertEquals(game.bidding.highestBid?.points, 260)
        assertTrue(player1.hand.cards.containsAll(game.dabb.cards))
    }

    private infix fun Game.apply(event: Event) {
        println(event.explain(this))
        event.apply(this)
        event.createChildEvents(this).forEach { this apply it }
    }

}