package org.jd.benoggl.mappers

import org.jd.benoggl.entities.BiddingEntity
import org.jd.benoggl.models.Bid
import org.jd.benoggl.models.Bidding
import org.jd.benoggl.models.Player
import org.jd.benoggl.resources.dtos.BiddingDto

fun BiddingDto.toModel(bids: Collection<Bid>, challengers: List<Player>, playerResolver: (String?) -> Player?) =
    Bidding(
        this.state!!,
        bids.toMutableList(),
        challengers.toMutableList(),
        playerResolver(this.highestBidderUid),
        this.highestBid
    )

fun Bidding.toDto() = BiddingDto(
    this.state,
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