package org.jd.benoggl.common.events

import kotlinx.serialization.Serializable
import org.jd.benoggl.common.models.Dabb
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Player

@Serializable
data class DabbPlacedEvent(val dabb: Dabb) : BackendOnlyEvent {

    override fun apply(game: Game) {
        game.dabb = dabb
    }

    override fun isVisibleForPlayer(game: Game, player: Player) = false

    override fun explain(game: Game) = "Placed dabb (face down): $dabb"

}