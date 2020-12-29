package org.jd.benoggl.persistence

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
    var hand: HandEntity? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var meld: MeldEntity? = null

    @OneToOne(mappedBy = "card", fetch = FetchType.LAZY)
    var move: MoveEntity? = null

}