package org.jd.benoggl.common.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Round

@Serializable
@SerialName("round-started")
object RoundStartedEvent : Event {

    override fun apply(game: Game) {
        val number = game.rounds.size
        game.rounds.add(Round(number))
    }

    override fun createChildEvents(game: Game): List<Event> {
        return listOf(
            CardsDealtEvent(),
            BiddingStartedEvent
        )
    }

    override fun explain(game: Game) = "Started new round"

}