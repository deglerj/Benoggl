package org.jd.benoggl.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.jd.benoggl.models.BiddingState
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(name = "BIDDING")
open class BiddingEntity : PanacheEntity() {

    companion object : PanacheCompanion<BiddingEntity, Long> {
    }

    @Column(nullable = false)
    lateinit var state: BiddingState

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "bidding")
    lateinit var round: RoundEntity

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "bidding")
    var bids: MutableCollection<BidEntity> = mutableListOf()

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "BIDDING_CHALLENGERS",
        joinColumns = [JoinColumn(name = "BIDDING_ID")],
        inverseJoinColumns = [JoinColumn(name = "PLAYER_ID")]
    )
    lateinit var challengers: MutableList<PlayerEntity>

    @ManyToOne(cascade = [CascadeType.ALL])
    var highestBidder: PlayerEntity? = null

    @Min(0)
    var highestBid: Int? = 0

}