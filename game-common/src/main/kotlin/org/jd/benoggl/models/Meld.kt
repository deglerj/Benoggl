package org.jd.benoggl.models

data class Meld(val cards: MutableCollection<Card>, val player: Player, val points: Int)
