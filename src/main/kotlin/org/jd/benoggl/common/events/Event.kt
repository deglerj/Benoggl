package org.jd.benoggl.common.events

import kotlinx.serialization.Serializable
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Player

@Serializable
sealed interface Event {

    fun apply(game: Game)

    fun createChildEvents(game: Game): List<Event> = listOf()

    fun isVisibleForPlayer(game: Game, player: Player): Boolean = true

    fun explain(game: Game): String

}