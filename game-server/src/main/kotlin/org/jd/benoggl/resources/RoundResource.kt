package org.jd.benoggl.resources

import org.jd.benoggl.entities.GameEntity
import org.jd.benoggl.entities.RoundEntity
import org.jd.benoggl.mappers.toDto
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.resources.dtos.RoundDto
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/game/{gameUid}/round")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
class RoundResource {

    @GET
    fun getRounds(@PathParam("gameUid") gameUid: String): List<RoundDto> {
        if (GameEntity.findByUid(gameUid) == null) {
            throw NotFoundException("Game $gameUid does not exist")
        }

        return RoundEntity.findByGameUid(gameUid).map { it.toModel().toDto() }
    }


    @GET
    @Path("/{roundNumber}")
    fun getRound(@PathParam("gameUid") gameUid: String, @PathParam("roundNumber") roundNumber: Int) =
        RoundEntity.findByNumber(roundNumber, gameUid)?.toModel()?.toDto()
            ?: throw NotFoundException("Round $roundNumber for game $gameUid does not exist")
}