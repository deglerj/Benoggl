package org.jd.benoggl.common.models

data class Meld(
    val player: Player,
    val cards: List<Card>,
    val points: Int
)
