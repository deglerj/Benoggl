package org.jd.benoggl.persistence

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.jd.benoggl.model.GameState
import org.jd.benoggl.model.GameType
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "GAME", uniqueConstraints = [UniqueConstraint(columnNames = ["uid"])])
open class GameEntity : PanacheEntity() {

    companion object : PanacheCompanion<GameEntity, Long> {
    }

    @Column(nullable = false)
    @NotBlank
    lateinit var uid: String

    @Column(nullable = false)
    lateinit var state: GameState

    @Column(nullable = false)
    lateinit var type: GameType

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "game")
    lateinit var rounds: MutableList<RoundEntity>

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "game")
    lateinit var players: MutableList<PlayerEntity>
}