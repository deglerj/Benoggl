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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "player")
    lateinit var playerHands: MutableCollection<PlayerHandEntity>

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "player")
    lateinit var moves: MutableCollection<MoveEntity>

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "GAME_ID", nullable = false)
    lateinit var game: GameEntity

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "winner")
    lateinit var wonTricks: MutableCollection<TrickEntity>

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "TRICK_PENDING_PLAYERS",
        joinColumns = [JoinColumn(name = "TRICK_ID")],
        inverseJoinColumns = [JoinColumn(name = "PLAYER_ID")]
    )
    var pendingInTrick: TrickEntity? = null

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "challengers")
    lateinit var challengerInBiddings: MutableCollection<BiddingEntity>

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "highestBidder")
    lateinit var wonBiddings: MutableCollection<BiddingEntity>

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "player")
    lateinit var melds: MutableCollection<MeldEntity>
}