package org.jd.benoggl.common.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jd.benoggl.common.models.Dabb
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Player
import org.jd.benoggl.common.models.findByUid

@Serializable
@SerialName("dabb-assigned")
data class DabbAssignedEvent(val playerUid: String) : Event {

    override fun apply(game: Game) {
        game.players.findByUid(playerUid).hand.cards += game.dabb.cards
        game.dabb = Dabb(listOf())
    }

    // Event could be visible, but there's no reason for players to see it
    override fun isVisibleForPlayer(game: Game, player: Player) = false

    override fun explain(game: Game) = "Assigned dabb to winner of bidding: $playerUid"

}