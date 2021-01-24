package org.jd.benoggl.models

data class Trick(
    val number: Int,
    var state: TrickState = TrickState.RUNNING,
    val moves: MutableList<Move>,
    val pendingPlayers: MutableList<Player>,
    var winner: Player?
)
