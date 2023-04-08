package org.jd.benoggl.common.events

import org.jd.benoggl.common.models.Dabb
import org.jd.benoggl.common.models.Game
import kotlin.test.Test
import kotlin.test.assertSame

internal class DabbPlacedEventTest {

    @Test
    fun apply_forAnyGame_assignsDabbToGame() {
        val game = Game()
        val dabb = Dabb(listOf())
        val sut = DabbPlacedEvent(dabb)

        sut.apply(game)

        assertSame(dabb, game.dabb)
    }

}