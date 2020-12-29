package org.jd.benoggl.persistence

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(name = "BID")
open class BidEntity : PanacheEntity() {

    companion object : PanacheCompanion<BidEntity, Long> {
    }

    @Column(nullable = false)
    @Min(0)
    var points: Int = -1

    @ManyToOne(optional = false)
    lateinit var player: PlayerEntity

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BIDDING_ID", nullable = false)
    lateinit var bidding: BiddingEntity

}