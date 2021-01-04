package org.jd.benoggl.mappers

import org.jd.benoggl.entities.CardEntity
import org.jd.benoggl.model.Card
import org.jd.benoggl.resources.dtos.CardDto

fun CardDto.toModel() = Card(
    this.suit!!,
    this.rank!!
)

fun Card.toDto() = CardDto(this.suit, this.rank)

fun CardEntity.toModel() = Card(
    this.suit,
    this.rank
)