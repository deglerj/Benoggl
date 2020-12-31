package org.jd.benoggl.rest

import org.jd.benoggl.mapper.toDto
import org.jd.benoggl.mapper.toModel
import org.jd.benoggl.persistence.PlayerHandEntity
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/game/{gameUid}/round/{roundNumber}/hand")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
class HandResource {

    @GET
    fun getPlayerHand(
        @HeaderParam("Player-UID") playerUid: String,
        @PathParam("gameUid") gameUid: String,
        @PathParam("roundNumber") roundNumber: Int
    ) =
        PlayerHandEntity.findPlayerHand(playerUid, gameUid, roundNumber)
            .hand
            .toModel()
            .toDto()


}