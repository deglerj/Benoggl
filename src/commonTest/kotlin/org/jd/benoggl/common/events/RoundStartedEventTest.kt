package org.jd.benoggl.common.events

import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Round
import org.jd.benoggl.common.models.RoundState
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RoundStartedEventTest {

    private val sut = RoundStartedEvent

    @Test
    fun apply_newGame_addsFirstRound() {
        val game = Game()

        sut.apply(game)

        assertEquals(game.rounds.size, 1)
        assertEquals(game.rounds[0].state, RoundState.NEW)
    }

    @Test
    fun apply_gameWithMultipleRounds_addsNextRound() {
        val game = Game(
            rounds = mutableListOf(Round(0), Round(1))
        )

        sut.apply(game)

        assertEquals(game.rounds.size, 3)
        assertEquals(game.rounds[2].state, RoundState.NEW)
    }


}