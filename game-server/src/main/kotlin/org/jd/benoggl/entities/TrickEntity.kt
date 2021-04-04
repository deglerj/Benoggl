package org.jd.benoggl.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.jd.benoggl.models.TrickState
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(name = "TRICK", uniqueConstraints = [UniqueConstraint(columnNames = ["NUMBER", "ROUND_ID"])])
open class TrickEntity : PanacheEntity() {

    companion object : PanacheCompanion<TrickEntity, Long> {

        fun findForRound(roundNumber: Int, gameUid: String) =
            TrickEntity.find("round.number = ?1 and round.game.uid = ?2", roundNumber, gameUid).list()

        fun findByNumber(trickNumber: Int, roundNumber: Int, gameUid: String) =
            TrickEntity.find(
                "round.number = ?1 and round.game.uid = ?2 and number = ?3",
                roundNumber,
                gameUid,
                trickNumber
            ).firstResult()
    }

    @Column(nullable = false)
    @Min(0)
    var number: Int = -1

    @Column(nullable = false)
    lateinit var state: TrickState

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "trick")
    @OrderBy("number ASC")
    lateinit var moves: MutableList<MoveEntity>

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "pendingInTrick")
    lateinit var pendingPlayers: MutableList<PlayerEntity>

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WINNER_PLAYER_ID")
    var winner: PlayerEntity? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUND_ID", nullable = false)
    lateinit var round: RoundEntity

}