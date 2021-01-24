package org.jd.benoggl.services

import org.jd.benoggl.entities.GameEntity
import org.jd.benoggl.entities.PlayerEntity
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.models.GameState
import org.jd.benoggl.models.GameType
import org.jd.benoggl.models.Player
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
@Transactional
class GameService {

    @Inject
    @field: Default
    internal lateinit var roundService: RoundService

    fun createAndStartGame(uid: String, type: GameType, players: List<Player>) {
        val entity = createGameEntity(uid, type)
        addPlayers(entity, players)
        entity.persist()

        startNewRound(entity)
    }

    private fun createGameEntity(uid: String, type: GameType): GameEntity {
        val game = GameEntity()
        game.uid = uid
        game.state = GameState.RUNNING
        game.type = type
        game.rounds = mutableListOf()
        game.players = mutableListOf()
        return game
    }

    private fun addPlayers(game: GameEntity, players: List<Player>) {
        players.map { it ->
            val player = PlayerEntity()
            player.uid = it.uid
            player.name = it.name
            player.game = game
            player
        }
            .forEach { game.players.add(it) }
    }

    private fun startNewRound(entity: GameEntity) {
        roundService.startNewRound(entity.toModel())
    }

}