package org.jd.benoggl.common.models

data class Bidding(
    val remainingPlayers: MutableList<Player>,
    val bids: MutableList<Bid> = mutableListOf()
) {
    val highestBid get() = bids.maxByOrNull(Bid::points)
}
