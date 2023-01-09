package org.jd.benoggl.common.models

import org.jd.benoggl.common.GameState
import java.util.*

data class Game(
    val uid: String = UUID.randomUUID().toString(),
    var state: GameState = GameState.RUNNING,
    val rounds: MutableList<Round> = mutableListOf(),
    val players: MutableList<Player> = mutableListOf(),
    var dabb: Dabb = Dabb(listOf()),
    var bidding: Bidding = Bidding(mutableListOf())
) {
    val currentRound get() = rounds.last()
}