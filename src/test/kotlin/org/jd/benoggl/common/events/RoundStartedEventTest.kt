package org.jd.benoggl.common.events

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Round
import org.jd.benoggl.common.models.RoundState
import org.junit.jupiter.api.Test

internal class RoundStartedEventTest {

    private val sut = RoundStartedEvent()

    @Test
    fun apply_newGame_addsFirstRound() {
        val game = Game(uid = "game", players = listOf())

        sut.apply(game)

        game.rounds shouldHaveSize 1
        game.rounds[0].state shouldBe RoundState.NEW
    }

    @Test
    fun apply_gameWithMultipleRounds_addsNextRound() {
        val game = Game(
            uid = "game", players = listOf(),
            rounds = mutableListOf(Round(0), Round(1))
        )

        sut.apply(game)

        game.rounds shouldHaveSize 3
        game.rounds[2].state shouldBe RoundState.NEW
    }


}