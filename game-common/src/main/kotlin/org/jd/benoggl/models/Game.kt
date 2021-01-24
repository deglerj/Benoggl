package org.jd.benoggl.models

data class Game(
    val uid: String,
    var state: GameState = GameState.RUNNING,
    val type: GameType = GameType.NORMAL,
    val rounds: MutableList<Round>,
    val players: List<Player>
)
