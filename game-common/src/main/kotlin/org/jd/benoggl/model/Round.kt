package org.jd.benoggl.model

data class Round(val number: Int, var state: RoundState = RoundState.BIDDING, var type: RoundType = RoundType.NORMAL, val bidding: Bidding, val melds: MutableCollection<Meld>, var trump: Suit?, var dabb: Hand?, val tricks: MutableList<Trick>)
