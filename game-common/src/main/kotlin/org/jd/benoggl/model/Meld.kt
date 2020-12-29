package org.jd.benoggl.model

data class Meld(val cards: MutableCollection<Card>, val player: Player, val points: Int)
