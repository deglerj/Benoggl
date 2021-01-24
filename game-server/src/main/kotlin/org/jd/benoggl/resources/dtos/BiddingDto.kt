package org.jd.benoggl.resources.dtos

import org.jd.benoggl.models.BiddingState
import javax.validation.constraints.NotNull

data class BiddingDto(

    @get:NotNull
    val state: BiddingState?,

    val highestBidderUid: String? = null,

    val highestBid: Int? = null
) {
}
