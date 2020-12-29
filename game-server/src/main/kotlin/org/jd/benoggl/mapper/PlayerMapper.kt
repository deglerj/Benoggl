package org.jd.benoggl.mapper

import org.jd.benoggl.model.Hand
import org.jd.benoggl.model.Player
import org.jd.benoggl.persistence.PlayerEntity
import org.jd.benoggl.rest.dtos.PlayerDto

fun PlayerDto.toModel(handResolver: (String?) -> Hand?) = Player(
    this.uid!!,
    this.name!!,
    handResolver(this.uid)!!
)

fun Player.toDto() = PlayerDto(this.uid, this.name)

fun PlayerEntity.toModel() = Player(
    this.uid,
    this.name,
    this.hand.toModel()
)