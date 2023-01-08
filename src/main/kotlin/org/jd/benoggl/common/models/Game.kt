package org.jd.benoggl.common.models

import org.jd.benoggl.common.GameState

data class Game(
    val uid: String,
    var state: GameState = GameState.RUNNING,
    val rounds: MutableList<Round> = mutableListOf(),
    val players: List<Player>,
    var dabb: Dabb = Dabb(listOf())
)