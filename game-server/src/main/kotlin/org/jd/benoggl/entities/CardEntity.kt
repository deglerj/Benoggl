package org.jd.benoggl.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import javax.persistence.*

@Entity
@Table(name = "CARD")
open class CardEntity : PanacheEntity() {

    companion object : PanacheCompanion<CardEntity> {
    }

    @Column(nullable = false)
    open lateinit var suit: Suit

    @Column(nullable = false)
    open lateinit var rank: Rank

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "HAND_CARDS",
        joinColumns = [JoinColumn(name = "HAND_ID")],
        inverseJoinColumns = [JoinColumn(name = "CARD_ID")]
    )
    open var hand: HandEntity? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "MELD_CARDS",
        joinColumns = [JoinColumn(name = "MELD_ID")],
        inverseJoinColumns = [JoinColumn(name = "CARD_ID")]
    )
    open var meld: MeldEntity? = null

    @OneToOne(mappedBy = "card", fetch = FetchType.LAZY)
    open var move: MoveEntity? = null

    override fun toString(): String {
        return "CardEntity[id=$id, "
    }

}