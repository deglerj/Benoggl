package org.jd.benoggl.resources.dtos

import org.jd.benoggl.models.GameState
import org.jd.benoggl.models.GameType
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class GameDto(

    @get:NotNull
    @get:NotBlank
    val uid: String?,

    @get:NotNull
    val state: GameState?,

    @get:NotNull
    val type: GameType?,

    @get:NotNull
    @get:NotEmpty
    val players: List<PlayerDto>?
) {
}
