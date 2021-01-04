package org.jd.benoggl.resources

import org.jd.benoggl.entities.GameEntity
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.model.GameState
import org.jd.benoggl.resources.dtos.GameDto
import org.jd.benoggl.resources.dtos.PlayerDto
import org.jd.benoggl.services.GameService
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Validator
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
class GameResource {

    @Inject
    @field: Default
    internal lateinit var validator: Validator

    @Inject
    @field: Default
    internal lateinit var gameService: GameService

    @PUT
    fun createNewGame(dto: GameDto) {
        validate(dto)

        gameService.createAndStartGame(
            dto.uid!!,
            dto.type!!,
            dto.players!!.map { it.toModel() }.toList()
        )
    }

    private fun validate(dto: GameDto) {
        validator.validateRestDto(dto)
        validateState(dto.state!!)
        validatePlayers(dto.players!!)
        validateUid(dto.uid!!)
    }

    private fun validateUid(uid: String) {
        if (GameEntity.findByUid(uid) != null) {
            throw BadRequestException("A game with the UID $uid already exists")
        }
    }

    private fun validatePlayers(players: List<PlayerDto>) {
        players.forEach(validator::validateRestDto)
        validatePlayerCount(players)
        validateUniquePlayerUids(players)
    }

    private fun validateUniquePlayerUids(players: List<PlayerDto>) {
        val distinctUidCount = players.map { it.uid }.distinct().size
        if (distinctUidCount != players.size) {
            throw BadRequestException("New games must be created with unique player UIDs")
        }
    }

    private fun validatePlayerCount(players: List<PlayerDto>) {
        if (players.size < 2 || players.size > 3) {
            throw BadRequestException("New games must be created with 2 or 3 players")
        }
    }

    private fun validateState(state: GameState) {
        if (state != GameState.RUNNING) {
            throw BadRequestException("New games must be created in RUNNING state")
        }
    }

}