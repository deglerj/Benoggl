package org.jd.benoggl.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(name = "MELD")
open class MeldEntity : PanacheEntity() {

    companion object : PanacheCompanion<MeldEntity, Long> {

        fun findForRound(roundNumber: Int, gameUid: String) =
            MeldEntity.find("round.number = ?1 and round.game.uid = ?2", roundNumber, gameUid).list()

        fun findForPlayer(playerUid: String, roundNumber: Int, gameUid: String) =
            MeldEntity.find(
                "player.uid = ? 1 and round.number = ?2 and round.game.uid = ?3",
                playerUid,
                roundNumber,
                gameUid
            ).firstResult()
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