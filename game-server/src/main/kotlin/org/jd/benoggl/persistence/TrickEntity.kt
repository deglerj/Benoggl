package org.jd.benoggl.persistence

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(name = "TRICK")
open class TrickEntity : PanacheEntity() {

    companion object : PanacheCompanion<TrickEntity, Long> {
    }

    @Column(nullable = false)
    @Min(0)
    var number: Int = -1

    @OneToOne(optional = false, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "CARD_ID", nullable = false)
    lateinit var card: CardEntity

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_ID", nullable = false)
    lateinit var player: PlayerEntity

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUND_ID", nullable = false)
    lateinit var round: RoundEntity

}