package org.jd.benoggl.common.events

import org.jd.benoggl.common.GameState
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.RoundState
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class EventsIntegrationTest {

    @Test
    fun simpleGameWithOneRound() {
        // New game
        val game = Game()
        assertEquals(game.state, GameState.RUNNING)
        assertEquals(game.rounds.size, 0)

        // Add three players
        game apply PlayerAddedEvent("player1")
        game apply PlayerAddedEvent("player2")
        game apply PlayerAddedEvent("player3")
        assertEquals(game.players.size, 3)
        assertContentEquals(game.players.map { it.name }, listOf("player1", "player2", "player3"))


        // Start round
        game apply RoundStartedEvent
        assertEquals(game.rounds.size, 1)
        game.players.forEach { player ->
            assertEquals(player.hand.cards.size, 12)
        }
        assertEquals(game.dabb.cards.size, 4)
        assertEquals(game.currentRound.state, RoundState.BIDDING)


        // Overwrite dabb and player hands
        // ...
    }

    private infix fun Game.apply(event: Event) {
        event.apply(this)
        event.createChildEvents(this).forEach { this apply it }
    }
}