package org.jd.benoggl.model

data class Game(val id: String, var state: GameState = GameState.RUNNING, val type: GameType = GameType.NORMAL, val rounds: MutableList<Round>, val players: List<Player>)
