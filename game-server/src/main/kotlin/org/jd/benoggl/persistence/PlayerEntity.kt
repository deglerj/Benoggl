package org.jd.benoggl.persistence

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "PLAYER")
open class PlayerEntity : PanacheEntity() {

    companion object : PanacheCompanion<PlayerEntity, Long> {
    }

    @Column(nullable = false)
    @NotBlank
    lateinit var uid: String

    @Column(nullable = false)
    @NotBlank
    lateinit var name: String

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "HAND_ID")
    lateinit var hand: HandEntity

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "player")
    lateinit var moves: MutableCollection<MoveEntity>

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GAME_ID", nullable = false)
    lateinit var game: GameEntity

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "challengers")
    lateinit var challengerInBiddings: MutableCollection<BiddingEntity>

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "highestBidder")
    lateinit var wonBiddings: MutableCollection<BiddingEntity>
}