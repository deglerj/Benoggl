package org.jd.benoggl.resources

import org.jd.benoggl.entities.TrickEntity
import org.jd.benoggl.mappers.toDto
import org.jd.benoggl.mappers.toModel
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/game/{gameUid}/round/{roundNumber}/trick")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
class TrickResource {

    @GET
    fun getTricks(
        @PathParam("gameUid") gameUid: String,
        @PathParam("roundNumber") roundNumber: Int
    ) = TrickEntity.findForRound(roundNumber, gameUid)
        .map { it.toModel().toDto() }

    @GET
    @Path(("/{trickNumber}"))
    fun getTrick(
        @PathParam("gameUid") gameUid: String,
        @PathParam("roundNumber") roundNumber: Int,
        @PathParam("trickNumber") trickNumber: Int
    ) = TrickEntity.findByNumber(roundNumber, gameUid, trickNumber)
        ?.toModel()
        ?.toDto()
        ?: throw NotFoundException("Could not find trick $trickNumber for round $roundNumber of game $gameUid")


}