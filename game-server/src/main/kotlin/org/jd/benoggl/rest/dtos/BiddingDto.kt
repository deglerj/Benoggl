package org.jd.benoggl.rest.dtos

import org.jd.benoggl.model.BiddingState
import javax.validation.constraints.NotNull

data class BiddingDto(

    @get:NotNull
    val state: BiddingState?,

    @get:NotNull
    val challengerUids: List<String>?,

    val highestBidderUid: String? = null,

    val highestBid: Int? = null
) {
}
