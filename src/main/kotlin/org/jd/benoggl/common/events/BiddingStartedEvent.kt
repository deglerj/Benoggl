package org.jd.benoggl.common.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.RoundState

@Serializable
@SerialName("bidding-started")
object BiddingStartedEvent : Event {

    override fun apply(game: Game) {
        game.currentRound.state = RoundState.BIDDING
    }

    override fun explain(game: Game) = "Bidding started"

}