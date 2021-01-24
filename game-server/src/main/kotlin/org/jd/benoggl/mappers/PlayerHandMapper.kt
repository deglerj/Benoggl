package org.jd.benoggl.mappers

import org.jd.benoggl.entities.PlayerHandEntity
import org.jd.benoggl.models.PlayerHand

fun PlayerHandEntity.toModel() = PlayerHand(
    this.player.toModel(),
    this.hand.toModel()
)