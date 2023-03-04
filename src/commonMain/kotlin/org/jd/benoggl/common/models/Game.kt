package org.jd.benoggl.common.models

import com.benasher44.uuid.uuid4
import org.jd.benoggl.common.GameState

data class Game(
    val uid: String = uuid4().toString(),
    var state: GameState = GameState.RUNNING,
    val rounds: MutableList<Round> = mutableListOf(),
    val players: MutableList<Player> = mutableListOf(),
    var dabb: Dabb = Dabb(listOf()),
    var bidding: Bidding = Bidding(mutableListOf())
) {
    val currentRound get() = rounds.last()
}