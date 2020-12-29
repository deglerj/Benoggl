package org.jd.benoggl.mapper

import org.jd.benoggl.model.Card
import org.jd.benoggl.persistence.CardEntity
import org.jd.benoggl.rest.dtos.CardDto

fun CardDto.toModel() = Card(
    this.suit!!,
    this.rank!!
)

fun Card.toDto() = CardDto(this.suit, this.rank)

fun CardEntity.toModel() = Card(
    this.suit,
    this.rank
)