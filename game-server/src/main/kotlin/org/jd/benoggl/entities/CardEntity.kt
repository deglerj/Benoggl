package org.jd.benoggl.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.jd.benoggl.model.Rank
import org.jd.benoggl.model.Suit
import javax.persistence.*

@Entity
@Table(name = "CARD")
open class CardEntity : PanacheEntity() {

    companion object : PanacheCompanion<CardEntity, Long> {
    }

    @Column(nullable = false)
    lateinit var suit: Suit

    @Column(nullable = false)
    lateinit var rank: Rank

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "HAND_CARDS",
        joinColumns = [JoinColumn(name = "HAND_ID")],
        inverseJoinColumns = [JoinColumn(name = "CARD_ID")]
    )
    var hand: HandEntity? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "MELD_CARDS",
        joinColumns = [JoinColumn(name = "MELD_ID")],
        inverseJoinColumns = [JoinColumn(name = "CARD_ID")]
    )
    var meld: MeldEntity? = null

    @OneToOne(mappedBy = "card", fetch = FetchType.LAZY)
    var move: MoveEntity? = null

}