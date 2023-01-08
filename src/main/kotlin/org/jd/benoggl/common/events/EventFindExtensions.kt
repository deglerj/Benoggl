package org.jd.benoggl.common.events

import org.jd.benoggl.common.models.Player
import org.jd.benoggl.common.models.UnknownPlayerException

internal fun Collection<Player>.findByUid(uid: String): Player {
    return find { it.uid == uid } ?: throw UnknownPlayerException("Unknown player $uid")
}