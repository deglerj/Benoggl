package org.jd.benoggl.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.jd.benoggl.models.HandType
import javax.persistence.*

@Entity
@Table(name = "HAND")
open class HandEntity : PanacheEntity() {

    companion object : PanacheCompanion<HandEntity> {

        fun findForPlayer(playerUid: String, roundNumber: Int, gameUid: String) =
            HandEntity.find(
                "playerHand.player.uid = ? 1 and playerHand.round.number = ?2 and playerHand.round.game.uid = ?3",
                playerUid,
                roundNumber,
                gameUid
            ).singleResult()
    }

    @Column(nullable = false)
    open lateinit var type: HandType

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "hand")
    open lateinit var cards: MutableCollection<CardEntity>

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "hand")
    open var playerHand: PlayerHandEntity? = null

    @OneToOne(mappedBy = "dabb", fetch = FetchType.LAZY)
    open var dabbInRound: RoundEntity? = null

    @OneToOne(mappedBy = "discard", fetch = FetchType.LAZY)
    open var discardInRound: RoundEntity? = null
}