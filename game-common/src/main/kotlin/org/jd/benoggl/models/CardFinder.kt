package org.jd.benoggl.models

fun Collection<Card>.findRanks(vararg ranks: Rank) = this.filter { ranks.contains(it.rank) }

fun Collection<Card>.findSuits(vararg suits: Suit) = this.filter { suits.contains(it.suit) }