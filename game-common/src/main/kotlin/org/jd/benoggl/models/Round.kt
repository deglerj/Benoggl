package org.jd.benoggl.models

data class Round(
    val number: Int,
    var state: RoundState = RoundState.BIDDING,
    var type: RoundType = RoundType.NORMAL,
    val bidding: Bidding,
    val melds: MutableCollection<Meld>,
    var trump: Suit?,
    val playerHands: MutableCollection<PlayerHand>,
    var dabb: Hand,
    val tricks: MutableList<Trick>
)
