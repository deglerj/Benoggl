package org.jd.benoggl.mapper

import org.jd.benoggl.model.*
import org.jd.benoggl.persistence.RoundEntity
import org.jd.benoggl.rest.dtos.RoundDto

fun RoundDto.toModel(
    bidding: Bidding,
    melds: Collection<Meld>,
    dabb: Hand,
    tricks: List<Trick>
) = Round(
    this.number!!,
    this.state!!,
    this.type!!,
    bidding,
    melds.toMutableList(),
    this.trump!!,
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
    this.dabb.toModel(),
    this.tricks.map { it.toModel() }.toMutableList()
)