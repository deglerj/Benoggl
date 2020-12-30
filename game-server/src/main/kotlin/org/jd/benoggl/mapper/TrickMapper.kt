package org.jd.benoggl.mapper

import org.jd.benoggl.model.Move
import org.jd.benoggl.model.Player
import org.jd.benoggl.model.Trick
import org.jd.benoggl.persistence.TrickEntity
import org.jd.benoggl.rest.dtos.TrickDto

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