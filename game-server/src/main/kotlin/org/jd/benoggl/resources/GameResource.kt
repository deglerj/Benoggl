package org.jd.benoggl.resources

import org.jd.benoggl.entities.GameEntity
import org.jd.benoggl.entities.PlayerEntity
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.model.GameState
import org.jd.benoggl.resources.dtos.GameDto
import org.jd.benoggl.resources.dtos.PlayerDto
import org.jd.benoggl.services.RoundService
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
    internal lateinit var roundService: RoundService

    // TODO move logic to GameMaster

    @PUT
    fun createNewGame(dto: GameDto) {
        validate(dto)

        val entity = createGame(dto)
        addPlayers(entity, dto.players!!)
        entity.persist()

        startNewRound(entity)
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

    private fun createGame(dto: GameDto): GameEntity {
        val game = GameEntity()
        game.uid = dto.uid!!
        game.state = dto.state!!
        game.type = dto.type!!
        game.rounds = mutableListOf()
        game.players = mutableListOf()
        return game
    }

    private fun addPlayers(game: GameEntity, players: List<PlayerDto>) {
        players.map { it ->
            val player = PlayerEntity()
            player.uid = it.uid!!
            player.name = it.name!!
            player.game = game
            player
        }
            .forEach { game.players.add(it) }
    }

    private fun startNewRound(entity: GameEntity) {
        roundService.startNewRound(entity.toModel())
    }

}