package org.jd.benoggl.server.entities

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("event")
data class EventEntity(
    @Id var id: Long? = null,
    @Column("game_uid") @NotBlank @Size(max = 38) var gameUid: String,
    @NotNull var data: String
) {
}