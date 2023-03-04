package org.jd.benoggl.common.events

import com.benasher44.uuid.uuid4
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.common.models.Player

@Serializable
@SerialName("player-added")
data class PlayerAddedEvent(val name: String, val uid: String = uuid4().toString()) : Event {

    override fun apply(game: Game) {
        game.players.add(Player(uid, name))
    }

    override fun explain(game: Game) = "Added new player \"$name\" (UID: $uid)"

}