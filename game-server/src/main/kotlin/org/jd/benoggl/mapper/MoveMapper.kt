package org.jd.benoggl.mapper

import org.jd.benoggl.model.Move
import org.jd.benoggl.model.Player
import org.jd.benoggl.persistence.MoveEntity
import org.jd.benoggl.rest.dtos.MoveDto

fun MoveDto.toModel(playerResolver: (String?) -> Player?) = Move(
    playerResolver(this.playerUid)!!,
    this.card!!.toModel()
)

fun Move.toDto() = MoveDto(this.player.uid, this.card.toDto())

fun MoveEntity.toModel() = Move(
    this.player.toModel(),
    this.card.toModel()
)