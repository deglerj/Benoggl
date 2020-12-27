package org.jd.benoggl.persistence

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.jd.benoggl.model.Rank
import org.jd.benoggl.model.Suit
import javax.persistence.Column
import javax.persistence.Entity

@Entity
open class CardEntity: PanacheEntity() {

    companion object: PanacheCompanion<CardEntity, Long> {
    }

    @Column(nullable = false)
    lateinit var suit: Suit

    @Column(nullable = false)
    lateinit var rank: Rank

}