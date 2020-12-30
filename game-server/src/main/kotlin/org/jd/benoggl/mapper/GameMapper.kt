package org.jd.benoggl.mapper

import org.jd.benoggl.model.Game
import org.jd.benoggl.model.Hand
import org.jd.benoggl.model.Round
import org.jd.benoggl.persistence.GameEntity
import org.jd.benoggl.rest.dtos.GameDto

fun GameDto.toModel(rounds: List<Round>, handResolver: (String?) -> Hand?) = Game(
    this.uid!!,
    this.state!!,
    this.type!!,
    rounds.toMutableList(),
    this.players!!.map { it.toModel(handResolver) }.toMutableList()
)

fun Game.toDto() = GameDto(
    this.uid,
    this.state,
    this.type,
    this.players.map { it.toDto() }.toList()
)

fun GameEntity.toModel() = Game(
    this.uid,
    this.state,
    this.type,
    this.rounds.map { it.toModel() }.toMutableList(),
    this.players.map { it.toModel() }.toMutableList()
)