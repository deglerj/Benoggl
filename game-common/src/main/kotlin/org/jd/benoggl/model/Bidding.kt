package org.jd.benoggl.model

data class Bidding(var state: BiddingState, val bids: MutableCollection<Bid>, val challengers: MutableList<Player>, var highesBidder: Player?)
