package org.jd.benoggl.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.jd.benoggl.models.GameState
import org.jd.benoggl.models.GameType
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "GAME", uniqueConstraints = [UniqueConstraint(columnNames = ["uid"])])
open class GameEntity : PanacheEntity() {

    companion object : PanacheCompanion<GameEntity> {

        fun findByUid(gameUid: String): GameEntity? =
            GameEntity.find("uid = ?1", gameUid).firstResult()
    }

    @Column(nullable = false)
    @NotBlank
    open lateinit var uid: String

    @Column(nullable = false)
    open lateinit var state: GameState

    @Column(nullable = false)
    open lateinit var type: GameType

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "game")
    @OrderBy("number ASC")
    open lateinit var rounds: MutableList<RoundEntity>

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "game")
    open lateinit var players: MutableList<PlayerEntity>
}