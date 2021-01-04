package org.jd.benoggl.rest

import org.jd.benoggl.mapper.toDto
import org.jd.benoggl.mapper.toModel
import org.jd.benoggl.persistence.PlayerHandEntity
import org.jd.benoggl.persistence.RoundEntity
import org.jd.benoggl.rest.dtos.HandDto
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
    ): HandDto {
        if (RoundEntity.findByNumber(roundNumber, gameUid) == null) {
            throw BadRequestException("Round $roundNumber for game $gameUid does not exist")
        }

        return PlayerHandEntity.findPlayerHand(playerUid, gameUid, roundNumber)
            .hand
            .toModel()
            .toDto()
    }


}