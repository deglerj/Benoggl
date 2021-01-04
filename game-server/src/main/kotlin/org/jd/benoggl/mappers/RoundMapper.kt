package org.jd.benoggl.mappers

import org.jd.benoggl.entities.RoundEntity
import org.jd.benoggl.model.*
import org.jd.benoggl.resources.dtos.RoundDto

fun RoundDto.toModel(
    bidding: Bidding,
    melds: Collection<Meld>,
    playerHands: Collection<PlayerHand>,
    dabb: Hand,
    tricks: List<Trick>
) = Round(
    this.number!!,
    this.state!!,
    this.type!!,
    bidding,
    melds.toMutableList(),
    this.trump!!,
    playerHands.toMutableList(),
    dabb,
    tricks.toMutableList()
)

fun Round.toDto() = RoundDto(this.number, this.state, this.type, this.trump)

fun RoundEntity.toModel() = Round(
    this.number,
    this.state,
    this.type,
    this.bidding.toModel(),
    this.melds.map { it.toModel() }.toMutableList(),
    this.trump,
    this.playerHands.map { it.toModel() }.toMutableList(),
    this.dabb.toModel(),
    this.tricks.map { it.toModel() }.toMutableList()
)