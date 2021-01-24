package org.jd.benoggl.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.jd.benoggl.models.HandType
import javax.persistence.*

@Entity
@Table(name = "HAND")
open class HandEntity : PanacheEntity() {

    companion object : PanacheCompanion<HandEntity, Long> {
    }

    @Column(nullable = false)
    lateinit var type: HandType

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "hand")
    lateinit var cards: MutableCollection<CardEntity>

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "hand")
    var playerHand: PlayerHandEntity? = null

    @OneToOne(mappedBy = "dabb", fetch = FetchType.LAZY)
    var round: RoundEntity? = null
}