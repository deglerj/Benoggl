package org.jd.benoggl.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "PLAYER")
open class PlayerEntity : PanacheEntity() {

    companion object : PanacheCompanion<PlayerEntity> {

        fun findByUid(playerUid: String, gameUid: String): PlayerEntity? =
            PlayerEntity.find("uid = ?1 and game.uid = ?2", playerUid, gameUid).firstResult()
    }

    @Column(nullable = false)
    @NotBlank
    open lateinit var uid: String

    @Column(nullable = false)
    @NotBlank
    open lateinit var name: String

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "player")
    open lateinit var playerHands: MutableCollection<PlayerHandEntity>

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "player")
    open lateinit var moves: MutableCollection<MoveEntity>

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "GAME_ID", nullable = false)
    open lateinit var game: GameEntity

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "winner")
    open lateinit var wonTricks: MutableCollection<TrickEntity>

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "TRICK_PENDING_PLAYERS",
        joinColumns = [JoinColumn(name = "TRICK_ID")],
        inverseJoinColumns = [JoinColumn(name = "PLAYER_ID")]
    )
    open var pendingInTrick: TrickEntity? = null

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "challengers")
    open lateinit var challengerInBiddings: MutableCollection<BiddingEntity>

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "highestBidder")
    open lateinit var wonBiddings: MutableCollection<BiddingEntity>

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "player")
    open lateinit var melds: MutableCollection<MeldEntity>
}