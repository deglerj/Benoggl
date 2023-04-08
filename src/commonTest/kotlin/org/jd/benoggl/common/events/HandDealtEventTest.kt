package org.jd.benoggl.common.events

import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Hand
import org.jd.benoggl.common.models.Player
import org.jd.benoggl.common.models.UnknownPlayerException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotSame
import kotlin.test.assertSame

internal class HandDealtEventTest {

    @Test
    fun apply_forValidPlayerUid_assignsHandToPlayer() {
        val player1 = Player("player1", "player1")
        val player2 = Player("player2", "player2")
        val game = Game(players = mutableListOf(player1, player2))
        val hand = Hand(mutableListOf())
        val sut = HandDealtEvent(hand, "player1")

        sut.apply(game)

        assertSame(hand, player1.hand)
        assertNotSame(hand, player2.hand)
    }

    @Test
    fun apply_forInvalidPlayerUid_shouldThrowUnknownPlayerException() {
        val player1 = Player("player1", "player1")
        val game = Game(players = mutableListOf(player1))
        val hand = Hand(mutableListOf())
        val sut = HandDealtEvent(hand, "player2")

        assertFailsWith<UnknownPlayerException> {
            sut.apply(game)
        }

        assertNotSame(hand, player1.hand)
    }

}