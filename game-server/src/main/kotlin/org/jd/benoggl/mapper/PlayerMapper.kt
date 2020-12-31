package org.jd.benoggl.mapper

import org.jd.benoggl.model.Player
import org.jd.benoggl.persistence.PlayerEntity
import org.jd.benoggl.rest.dtos.PlayerDto

fun PlayerDto.toModel() = Player(
    this.uid!!,
    this.name!!
)

fun Player.toDto() = PlayerDto(this.uid, this.name)

fun PlayerEntity.toModel() = Player(
    this.uid,
    this.name
)