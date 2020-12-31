package org.jd.benoggl.mapper

import org.jd.benoggl.model.PlayerHand
import org.jd.benoggl.persistence.PlayerHandEntity

fun PlayerHandEntity.toModel() = PlayerHand(
    this.player.toModel(),
    this.hand.toModel()
)