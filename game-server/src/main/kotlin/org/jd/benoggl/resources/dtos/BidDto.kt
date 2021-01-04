package org.jd.benoggl.resources.dtos

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class BidDto(

    @get:NotNull
    @get:Min(0)
    val points: Int?,

    @get:NotNull
    @get:NotBlank
    val playerUid: String?
) {
}
