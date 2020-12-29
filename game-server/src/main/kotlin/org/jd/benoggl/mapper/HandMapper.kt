package org.jd.benoggl.mapper

import org.jd.benoggl.model.Hand
import org.jd.benoggl.persistence.HandEntity
import org.jd.benoggl.rest.dtos.HandDto

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