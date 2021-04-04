package org.jd.benoggl.resources

import org.jd.benoggl.entities.MoveEntity
import org.jd.benoggl.entities.PlayerEntity
import org.jd.benoggl.mappers.toDto
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.models.Player
import org.jd.benoggl.resources.dtos.MoveDto
import org.jd.benoggl.services.MoveService
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Validator
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/game/{gameUid}/round/{roundNumber}/trick/{trickNumber}/move")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
class MoveResource {

    @Inject
    @field: Default
    internal lateinit var validator: Validator

    @Inject
    @field: Default
    internal lateinit var moveService: MoveService

    @GET
    fun getMoves(
        @PathParam("gameUid") gameUid: String,
        @PathParam("roundNumber") roundNumber: Int,
        @PathParam("trickNumber") trickNumber: Int
    ) = MoveEntity.findForTrick(trickNumber, roundNumber, gameUid)
        .map { it.toModel().toDto() }

    @GET
    @Path("/{moveNumber}")
    fun getMove(
        @PathParam("gameUid") gameUid: String,
        @PathParam("roundNumber") roundNumber: Int,
        @PathParam("trickNumber") trickNumber: Int,
        @PathParam("moveNumber") moveNumber: Int
    ) = MoveEntity.findByNumber(moveNumber, trickNumber, roundNumber, gameUid)
        ?.toModel()
        ?.toDto()
        ?: throw NotFoundException("Could not find move $moveNumber of trick $trickNumber in round $roundNumber of game $gameUid")

    @PUT
    fun addMove(
        @PathParam("gameUid") gameUid: String,
        @PathParam("roundNumber") roundNumber: Int,
        @PathParam("trickNumber") trickNumber: Int,
        move: MoveDto
    ) {
        validator.validateRestDto(move)

        val playerResolver: (String) -> Player = { findPlayer(it, gameUid) }
        val moveModel = move.toModel(playerResolver)
        moveService.placeMove(moveModel, trickNumber, roundNumber, gameUid)
    }

    private fun findPlayer(playerUid: String, gameUid: String) = PlayerEntity.findByUid(playerUid, gameUid)?.toModel()
        ?: throw BadRequestException("Player $playerUid can not be found in game $gameUid")
}