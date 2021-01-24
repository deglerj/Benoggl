package org.jd.benoggl.models

data class Hand(val cards: MutableCollection<Card>, val type: HandType = HandType.NORMAL)
