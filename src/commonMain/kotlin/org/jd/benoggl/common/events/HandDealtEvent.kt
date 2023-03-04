package org.jd.benoggl.common.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Hand
import org.jd.benoggl.common.models.Player
import org.jd.benoggl.common.models.findByUid

@Serializable
@SerialName("hand-dealt")
data class HandDealtEvent(val hand: Hand, val playerUid: String) : Event {

    override fun apply(game: Game) {
        game.players.findByUid(playerUid).hand = hand
    }

    override fun isVisibleForPlayer(game: Game, player: Player) = player.uid == playerUid

    override fun explain(game: Game) = "Dealt cards to player $playerUid: ${hand.cards}"

}