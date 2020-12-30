package org.jd.benoggl.persistence

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.*

@Entity
@Table(name = "MOVE")
open class MoveEntity : PanacheEntity() {

    companion object : PanacheCompanion<MoveEntity, Long> {
    }

    @OneToOne(optional = false, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "CARD_ID", nullable = false)
    lateinit var card: CardEntity

    @ManyToOne(optional = false)
    @JoinColumn(name = "PLAYER_ID", nullable = false)
    lateinit var player: PlayerEntity

    @ManyToOne(optional = false)
    @JoinColumn(name = "TRICK_ID", nullable = false)
    lateinit var trick: TrickEntity
}