package org.jd.benoggl.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(name = "MELD")
open class MeldEntity : PanacheEntity() {

    companion object : PanacheCompanion<MeldEntity, Long> {
    }

    @Column(nullable = false)
    @Min(0)
    var points: Int = -1

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "meld")
    lateinit var cards: MutableCollection<CardEntity>

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUND_ID", nullable = false)
    lateinit var round: RoundEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_ID", nullable = false)
    lateinit var player: PlayerEntity

}