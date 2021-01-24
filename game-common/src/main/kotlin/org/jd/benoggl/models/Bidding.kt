package org.jd.benoggl.models

data class Bidding(
    var state: BiddingState,
    val bids: MutableCollection<Bid>,
    val challengers: MutableList<Player>,
    var highestBidder: Player?,
    var highestBid: Int?
)
