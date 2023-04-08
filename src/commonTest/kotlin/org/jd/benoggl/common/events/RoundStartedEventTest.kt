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

        assertEquals(1, game.rounds.size)
        assertEquals(RoundState.NEW, game.rounds[0].state)
    }

    @Test
    fun apply_gameWithMultipleRounds_addsNextRound() {
        val game = Game(
            rounds = mutableListOf(Round(0), Round(1))
        )

        sut.apply(game)

        assertEquals(3, game.rounds.size)
        assertEquals(RoundState.NEW, game.rounds[2].state)
    }


}