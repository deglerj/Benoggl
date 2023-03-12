package org.jd.benoggl.common.models

fun Collection<Player>.findByUid(uid: String): Player {
    return find { it.uid == uid } ?: throw UnknownPlayerException("Unknown player $uid")
}

fun Collection<Card>.findRanks(vararg ranks: Rank) = this.filter { ranks.contains(it.rank) }

fun Collection<Card>.findSuits(vararg suits: Suit) = this.filter { suits.contains(it.suit) }

fun Collection<Card>.withoutSuits(vararg suits: Suit) = this.filterNot { suits.contains(it.suit) }

fun Collection<Card>.countFourOfAKind(rank: Rank): Int {
    val cardsWithRank = this.findRanks(rank)
    return Suit.values()
        .map { cardsWithRank.findSuits(it).size }
        .min()
}