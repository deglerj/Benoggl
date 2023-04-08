package org.jd.benoggl.common.events

import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Round
import org.jd.benoggl.common.models.RoundState
import kotlin.test.Test
import kotlin.test.assertEquals

internal class BiddingStartedEventTest {

    @Test
    fun apply_forCurrentRound_changesStateToBidding() {
        val currentRound = Round(1)
        val game = Game(rounds = mutableListOf(currentRound))
        val sut = BiddingStartedEvent

        sut.apply(game)

        assertEquals(RoundState.BIDDING, currentRound.state)
    }

}