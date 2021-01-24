package org.jd.benoggl.mappers

import org.jd.benoggl.entities.TrickEntity
import org.jd.benoggl.models.Move
import org.jd.benoggl.models.Player
import org.jd.benoggl.models.Trick
import org.jd.benoggl.resources.dtos.TrickDto

fun TrickDto.toModel(moves: List<Move>, playerResolver: (String?) -> Player?) = Trick(
    this.number!!,
    this.state!!,
    moves.toMutableList(),
    this.pendingPlayerUids!!.mapNotNull(playerResolver).toMutableList(),
    playerResolver(this.winnerUid)
)

fun Trick.toDto() = TrickDto(
    this.number,
    this.state,
    this.pendingPlayers.map { it.uid }.toList(),
    this.winner?.uid
)

fun TrickEntity.toModel() = Trick(
    this.number,
    this.state,
    this.moves.map { it.toModel() }.toMutableList(),
    this.pendingPlayers.map { it.toModel() }.toMutableList(),
    this.winner?.toModel()
)