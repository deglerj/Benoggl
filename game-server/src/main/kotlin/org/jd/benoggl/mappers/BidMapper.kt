package org.jd.benoggl.mappers

import org.jd.benoggl.entities.BidEntity
import org.jd.benoggl.models.Bid
import org.jd.benoggl.models.Player
import org.jd.benoggl.resources.dtos.BidDto

fun BidDto.toModel(playerResolver: (String?) -> Player?) = Bid(
    this.points!!,
    playerResolver(this.playerUid)!!
)

fun Bid.toDto() = BidDto(this.points, this.player.uid)

fun BidEntity.toModel() = Bid(
    this.points,
    this.player.toModel()
)