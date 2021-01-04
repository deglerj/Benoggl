package org.jd.benoggl.mappers

import org.jd.benoggl.entities.PlayerEntity
import org.jd.benoggl.model.Player
import org.jd.benoggl.resources.dtos.PlayerDto

fun PlayerDto.toModel() = Player(
    this.uid!!,
    this.name!!
)

fun Player.toDto() = PlayerDto(this.uid, this.name)

fun PlayerEntity.toModel() = Player(
    this.uid,
    this.name
)