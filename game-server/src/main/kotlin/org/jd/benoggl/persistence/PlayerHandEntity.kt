package org.jd.benoggl.persistence

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.*

@Entity
@Table(name = "PLAYER_HAND")
open class PlayerHandEntity : PanacheEntity() {

    companion object : PanacheCompanion<PlayerHandEntity, Long> {
    }

    @ManyToOne(cascade = [CascadeType.ALL], optional = false)
    @JoinColumn(name = "PLAYER_ID", nullable = false)
    lateinit var player: PlayerEntity

    @OneToOne(cascade = [CascadeType.ALL], optional = false)
    @JoinColumn(name = "HAND_ID", nullable = false)
    lateinit var hand: HandEntity

    @ManyToOne(cascade = [CascadeType.ALL], optional = false)
    @JoinColumn(name = "ROUND_ID", nullable = false)
    lateinit var round: RoundEntity


}