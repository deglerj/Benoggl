package org.jd.benoggl.common.models

fun Collection<Player>.findByUid(uid: String): Player {
    return find { it.uid == uid } ?: throw UnknownPlayerException("Unknown player $uid")
}
