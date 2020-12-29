package org.jd.benoggl.persistence

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(name = "MELD")
open class MeldEntity : PanacheEntity() {

    companion object : PanacheCompanion<MeldEntity, Long> {
    }

    @Column(nullable = false)
    @Min(0)
    var points: Int = -1

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "MELD_CARDS",
        joinColumns = [JoinColumn(name = "MELD_ID")],
        inverseJoinColumns = [JoinColumn(name = "CARD_ID")]
    )
    lateinit var cards: MutableCollection<CardEntity>

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUND_ID", nullable = false)
    lateinit var round: RoundEntity

}