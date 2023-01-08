package org.jd.benoggl.common.events

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Hand
import org.jd.benoggl.common.models.Player
import org.jd.benoggl.common.models.UnknownPlayerException
import org.junit.jupiter.api.Test

internal class HandDealtEventTest {

    @Test
    fun apply_forValidPlayerUid_assignsHandToPlayer() {
        val player1 = Player("player1", "player1")
        val player2 = Player("player2", "player2")
        val game = Game(players = mutableListOf(player1, player2))
        val hand = Hand(mutableListOf())
        val sut = HandDealtEvent(hand, "player1")

        sut.apply(game)

        player1.hand shouldBeSameInstanceAs hand
        player2.hand shouldNotBeSameInstanceAs hand
    }

    @Test
    fun apply_forInvalidPlayerUid_shouldThrowUnknownPlayerException() {
        val player1 = Player("player1", "player1")
        val game = Game(players = mutableListOf(player1))
        val hand = Hand(mutableListOf())
        val sut = HandDealtEvent(hand, "player2")

        shouldThrow<UnknownPlayerException> {
            sut.apply(game)
        }

        player1.hand shouldNotBeSameInstanceAs hand
    }

}