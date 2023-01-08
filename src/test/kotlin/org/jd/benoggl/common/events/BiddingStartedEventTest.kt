package org.jd.benoggl.common.events

import io.kotest.matchers.shouldBe
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Round
import org.jd.benoggl.common.models.RoundState
import org.junit.jupiter.api.Test

internal class BiddingStartedEventTest {

    @Test
    fun apply_forCurrentRound_changesStateToBidding() {
        val currentRound = Round(1)
        val game = Game(rounds = mutableListOf(currentRound))
        val sut = BiddingStartedEvent

        sut.apply(game)

        currentRound.state shouldBe RoundState.BIDDING
    }

}