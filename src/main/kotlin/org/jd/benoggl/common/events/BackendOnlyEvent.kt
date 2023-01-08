package org.jd.benoggl.common.events

import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Player

interface BackendOnlyEvent : Event {

    fun createChildEvents(game: Game): List<Event> = listOf()

    override fun isVisibleForPlayer(game: Game, player: Player): Boolean = false

}