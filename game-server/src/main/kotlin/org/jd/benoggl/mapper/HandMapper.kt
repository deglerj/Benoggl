package org.jd.benoggl.mapper

import org.jd.benoggl.model.Hand
import org.jd.benoggl.persistence.HandEntity
import org.jd.benoggl.rest.HandDto

fun HandDto.toModel() = Hand(
    type = this.type,
    cards = this.cards.map { it.toModel() }.toMutableList()
)

fun Hand.toDto(): HandDto {
    val dto = HandDto()
    dto.type = this.type
    dto.cards = this.cards.map { it.toDto() }.toMutableList()
    return dto
}

fun Hand.toEntity(): HandEntity {
    val entity = HandEntity()
    entity.type = this.type
    entity.cards = this.cards.map{ it.toEntity() }.toMutableList()
    return entity
}

fun HandEntity.toModel() = Hand(
    type = this.type,
    cards = this.cards.map { it.toModel() }.toMutableList()
)