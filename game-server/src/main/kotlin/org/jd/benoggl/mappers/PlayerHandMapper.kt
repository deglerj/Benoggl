package org.jd.benoggl.mappers

import org.jd.benoggl.entities.PlayerHandEntity
import org.jd.benoggl.model.PlayerHand

fun PlayerHandEntity.toModel() = PlayerHand(
    this.player.toModel(),
    this.hand.toModel()
)