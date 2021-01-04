package org.jd.benoggl.persistence

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.*
import javax.persistence.PersistenceException

@Entity
@Table(name = "PLAYER_HAND")
open class PlayerHandEntity : PanacheEntity() {

    companion object : PanacheCompanion<PlayerHandEntity, Long> {

        fun findPlayerHand(playerUid: String, gameUid: String, roundNumber: Int): PlayerHandEntity {
            val entities = PlayerHandEntity.find(
                "player.uid = ?1 and round.game.uid = ?2 and round.number = ?3",
                playerUid, gameUid, roundNumber
            ).list()
            when {
                entities.isEmpty() -> {
                    throw PersistenceException("No hands for player $playerUid in round $roundNumber of game $gameUid")
                }
                entities.size > 1 -> {
                    throw PersistenceException("More than one hand for player $playerUid in round $roundNumber of game $gameUid")
                }
                else -> {
                    return entities[0]
                }
            }
        }
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "PLAYER_ID", nullable = false)
    lateinit var player: PlayerEntity

    @OneToOne(cascade = [CascadeType.ALL], optional = false)
    @JoinColumn(name = "HAND_ID", nullable = false)
    lateinit var hand: HandEntity

    @ManyToOne(optional = false)
    @JoinColumn(name = "ROUND_ID", nullable = false)
    lateinit var round: RoundEntity


}