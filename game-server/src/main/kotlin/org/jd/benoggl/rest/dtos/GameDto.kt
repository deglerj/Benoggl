package org.jd.benoggl.rest.dtos

import org.jd.benoggl.model.GameState
import org.jd.benoggl.model.GameType
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
