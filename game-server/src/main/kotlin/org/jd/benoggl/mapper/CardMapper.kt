package org.jd.benoggl.mapper

import org.jd.benoggl.model.Card
import org.jd.benoggl.persistence.CardEntity
import org.jd.benoggl.rest.CardDto

fun CardDto.toModel() = Card(
    suit = this.suit,
    rank = this.rank
)

fun Card.toDto(): CardDto {
    val dto = CardDto()
    dto.suit = this.suit
    dto.rank = this.rank
    return dto
}

fun Card.toEntity(): CardEntity {
    val entity = CardEntity()
    entity.suit = this.suit
    entity.rank = this.rank
    return entity
}

fun CardEntity.toModel() = Card(
    suit = this.suit,
    rank = this.rank
)