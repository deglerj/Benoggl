package org.jd.benoggl.common.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Player
import java.util.*

@Serializable
@SerialName("player-added")
data class PlayerAddedEvent(val name: String, val uid: String = UUID.randomUUID().toString()) : Event {

    override fun apply(game: Game) {
        game.players.add(Player(uid, name))
    }

    override fun explain(game: Game) = "Added new player \"$name\" (UID: $uid)"

}