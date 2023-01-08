package org.jd.benoggl.common.events

import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Player

interface Event {

    fun apply(game: Game)

    fun isVisibleForPlayer(game: Game, player: Player): Boolean = true

    fun explain(game: Game): String

}