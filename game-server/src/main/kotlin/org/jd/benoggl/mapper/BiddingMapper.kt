package org.jd.benoggl.mapper

import org.jd.benoggl.model.Bid
import org.jd.benoggl.model.Bidding
import org.jd.benoggl.model.Player
import org.jd.benoggl.persistence.BiddingEntity
import org.jd.benoggl.rest.dtos.BiddingDto

fun BiddingDto.toModel(bids: Collection<Bid>, playerResolver: (String?) -> Player?) = Bidding(
    this.state!!,
    bids.toMutableList(),
    this.challengerUids!!.mapNotNull(playerResolver).toMutableList(),
    playerResolver(this.highestBidderUid),
    this.highestBid
)

fun Bidding.toDto() = BiddingDto(
    this.state,
    this.challengers.map { it.uid }.toList(),
    this.highestBidder?.uid,
    this.highestBid
)

fun BiddingEntity.toModel() = Bidding(
    this.state,
    this.bids.map { it.toModel() }.toMutableList(),
    this.challengers.map { it.toModel() }.toMutableList(),
    this.highestBidder?.toModel(),
    this.highestBid
)