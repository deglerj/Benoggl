package org.jd.benoggl.common.events

import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.jd.benoggl.common.models.Dabb
import org.jd.benoggl.common.models.Game
import org.junit.jupiter.api.Test

internal class DabbPlacedEventTest {

    @Test
    fun apply_forAnyGame_assignsDabbToGame() {
        val game = Game()
        val dabb = Dabb(listOf())
        val sut = DabbPlacedEvent(dabb)

        sut.apply(game)

        game.dabb shouldBeSameInstanceAs dabb
    }

}