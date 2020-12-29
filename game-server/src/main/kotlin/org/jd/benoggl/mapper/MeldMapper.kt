package org.jd.benoggl.mapper

import org.jd.benoggl.model.Meld
import org.jd.benoggl.model.Player
import org.jd.benoggl.persistence.MeldEntity
import org.jd.benoggl.rest.dtos.MeldDto

fun MeldDto.toModel(playerResolver: (String?) -> Player?) = Meld(
    this.cards!!.map { it.toModel() }.toMutableList(),
    playerResolver(this.playerUid)!!,
    this.points!!
)

fun Meld.toDto() = MeldDto(this.cards.map { it.toDto() }, this.player.uid, this.points)

fun MeldEntity.toModel() = Meld(
    this.cards.map { it.toModel() }.toMutableList(),
    this.player.toModel(),
    this.points
)