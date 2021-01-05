package org.jd.benoggl.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.jd.benoggl.model.RoundState
import org.jd.benoggl.model.RoundType
import org.jd.benoggl.model.Suit
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(name = "ROUND")
open class RoundEntity : PanacheEntity() {

    companion object : PanacheCompanion<RoundEntity, Long> {

        fun findByGameUid(gameUid: String): List<RoundEntity> =
            RoundEntity.find("game.uid = ?1 order by number", gameUid).list()

        fun findByNumber(number: Int, gameUid: String): RoundEntity? =
            RoundEntity.find("number = ?1 and game.uid = ?2", number, gameUid).firstResult()

    }

    @Column(nullable = false)
    @Min(0)
    var number: Int = -1

    @Column(nullable = false)
    lateinit var state: RoundState

    @Column(nullable = false)
    lateinit var type: RoundType

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GAME_ID", nullable = false)
    lateinit var game: GameEntity

    @OneToOne(optional = false, cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "BIDDING_ID", nullable = false)
    lateinit var bidding: BiddingEntity

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "round")
    var melds: MutableCollection<MeldEntity> = mutableListOf()

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "round")
    lateinit var playerHands: MutableCollection<PlayerHandEntity>

    var trump: Suit? = null

    @ManyToOne(cascade = [CascadeType.ALL], optional = false)
    @JoinColumn(name = "DABB_HAND_ID", nullable = false)
    lateinit var dabb: HandEntity

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "round")
    var tricks: MutableCollection<TrickEntity> = mutableListOf()

}