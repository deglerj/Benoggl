package org.jd.benoggl.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(name = "MOVE")
open class MoveEntity : PanacheEntity() {

    companion object : PanacheCompanion<MoveEntity, Long> {

        fun findForTrick(trickNumber: Int, roundNumber: Int, gameUid: String) =
            TrickEntity.find(
                "round.number = ?1 and round.game.uid = ?2 and number = ?3",
                roundNumber,
                gameUid,
                trickNumber
            ).list()

        fun findByNumber(moveNumber: Int, trickNumber: Int, roundNumber: Int, gameUid: String) =
            TrickEntity.find(
                "round.number = ?1 and round.game.uid = ?2 and number = ?3 and move.number = ?4",
                roundNumber,
                gameUid,
                trickNumber,
                moveNumber
            ).firstResult()
    }

    @Column(nullable = false)
    @Min(0)
    var number: Int = -1

    @OneToOne(optional = false, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "CARD_ID", nullable = false)
    lateinit var card: CardEntity

    @ManyToOne(optional = false)
    @JoinColumn(name = "PLAYER_ID", nullable = false)
    lateinit var player: PlayerEntity

    @ManyToOne(optional = false)
    @JoinColumn(name = "TRICK_ID", nullable = false)
    lateinit var trick: TrickEntity
}