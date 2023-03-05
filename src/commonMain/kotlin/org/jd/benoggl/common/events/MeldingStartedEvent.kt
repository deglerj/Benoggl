package org.jd.benoggl.common.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Melding
import org.jd.benoggl.common.models.RoundState

@Serializable
@SerialName("melding-started")
object MeldingStartedEvent : Event {

    override fun apply(game: Game) {
        game.currentRound.state = RoundState.MELDING
        game.melding = Melding(mutableListOf())
    }

    override fun explain(game: Game) = "Melding started (and bidding ended)"

}