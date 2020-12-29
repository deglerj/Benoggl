package org.jd.benoggl.mapper

import org.jd.benoggl.model.Bid
import org.jd.benoggl.model.Player
import org.jd.benoggl.persistence.BidEntity
import org.jd.benoggl.rest.dtos.BidDto

fun BidDto.toModel(playerResolver: (String?) -> Player?) = Bid(
    this.points!!,
    playerResolver(this.playerUid)!!
)

fun Bid.toDto() = BidDto(this.points, this.player.uid)

fun BidEntity.toModel() = Bid(
    this.points,
    this.player.toModel()
)