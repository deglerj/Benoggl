package org.jd.benoggl.mappers

import org.jd.benoggl.entities.MoveEntity
import org.jd.benoggl.models.Move
import org.jd.benoggl.models.Player
import org.jd.benoggl.resources.dtos.MoveDto

fun MoveDto.toModel(playerResolver: (String?) -> Player?) = Move(
    playerResolver(this.playerUid)!!,
    this.card!!.toModel()
)

fun Move.toDto() = MoveDto(this.player.uid, this.card.toDto())

fun MoveEntity.toModel() = Move(
    this.player.toModel(),
    this.card.toModel()
)