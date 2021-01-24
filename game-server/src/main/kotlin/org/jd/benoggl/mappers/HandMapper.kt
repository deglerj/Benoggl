package org.jd.benoggl.mappers

import org.jd.benoggl.entities.HandEntity
import org.jd.benoggl.models.Hand
import org.jd.benoggl.resources.dtos.HandDto

fun HandDto.toModel() = Hand(
    type = this.type!!,
    cards = this.cards!!.map { it.toModel() }.toMutableList()
)

fun Hand.toDto() = HandDto(
    this.type,
    this.cards.map { it.toDto() }.toList()
)

fun HandEntity.toModel() = Hand(
    type = this.type,
    cards = this.cards.map { it.toModel() }.toMutableList()
)