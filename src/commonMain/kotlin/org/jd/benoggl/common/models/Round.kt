package org.jd.benoggl.common.models

data class Round(
    val number: Int,
    var state: RoundState = RoundState.NEW,
)
