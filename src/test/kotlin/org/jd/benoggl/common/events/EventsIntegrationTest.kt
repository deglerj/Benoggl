package org.jd.benoggl.common.events

import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.jd.benoggl.common.GameState
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.RoundState
import org.junit.jupiter.api.Test

internal class EventsIntegrationTest {

    @Test
    fun simpleGameWithOneRound() {
        // New game
        val game = Game()
        game.state shouldBe GameState.RUNNING
        game.rounds shouldHaveSize 0

        // Add three players
        game apply PlayerAddedEvent("player1")
        game apply PlayerAddedEvent("player2")
        game apply PlayerAddedEvent("player3")
        game.players shouldHaveSize 3
        game.players.map { it.name } shouldContainInOrder listOf("player1", "player2", "player3")


        // Start round
        game apply RoundStartedEvent
        game.rounds shouldHaveSize 1
        game.players.forEach { player ->
            player.hand.cards shouldHaveSize 12
        }
        game.dabb.cards shouldHaveSize 4
        game.currentRound.state shouldBe RoundState.BIDDING


        // Overwrite dabb and player hands
        // ...
    }

    private infix fun Game.apply(event: Event) {
        event.apply(this)
        event.createChildEvents(this).forEach { this apply it }
    }
}