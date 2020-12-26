package org.jd.benoggl.model

data class Hand(val cards: MutableCollection<Card>, val type: HandType = HandType.NORMAL)
