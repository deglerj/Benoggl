package org.jd.benoggl.resources.dtos

import org.jd.benoggl.model.TrickState
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class TrickDto(

    @get:NotNull
    @get:Min(0)
    val number: Int?,

    @get:NotNull
    val state: TrickState?,

    @get:NotNull
    val pendingPlayerUids: List<String>?,

    val winnerUid: String?
) {
}
