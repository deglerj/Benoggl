package org.jd.benoggl.persistence

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.jd.benoggl.model.HandType
import javax.persistence.*

@Entity
@Table(name = "HAND")
open class HandEntity : PanacheEntity() {

    companion object : PanacheCompanion<HandEntity, Long> {
    }

    @Column(nullable = false)
    lateinit var type: HandType

    @OneToMany(cascade = [CascadeType.ALL])
    lateinit var cards: MutableCollection<CardEntity>

    @OneToOne(mappedBy = "hand", fetch = FetchType.LAZY)
    var player: PlayerEntity? = null

    @OneToOne(mappedBy = "dabb", fetch = FetchType.LAZY)
    var round: RoundEntity? = null
}