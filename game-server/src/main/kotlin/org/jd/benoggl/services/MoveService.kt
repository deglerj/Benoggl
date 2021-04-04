package org.jd.benoggl.services

import org.jd.benoggl.entities.CardEntity
import org.jd.benoggl.entities.MoveEntity
import org.jd.benoggl.entities.PlayerEntity
import org.jd.benoggl.entities.TrickEntity
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Move
import org.jd.benoggl.models.TrickState
import org.jd.benoggl.rules.tricktaking.MoveEvaluationResult
import org.jd.benoggl.rules.tricktaking.MoveEvaluator
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
@Transactional
class MoveService {

    @Inject
    @field: Default
    internal lateinit var trickService: TrickService

    val evaluator = MoveEvaluator()

    fun placeMove(move: Move, trickNumber: Int, roundNumber: Int, gameUid: String) {
        val trick = findTrickEntity(trickNumber, roundNumber, gameUid)
        if (trick.state != TrickState.RUNNING) {
            throw IllegalMoveException("Trick $trickNumber for round $roundNumber of game $gameUid is not running (state: ${trick.state})")
        }

        val player = PlayerEntity.findByUid(move.player.uid, gameUid)
            ?: throw IllegalMoveException("Could not find player ${move.player.uid} in game $gameUid")

        ensureMoveIsValid(move, trick, player)

        persistMove(move, trick, player)
        removePlayerFromTrick(player, trick)

        if (isTrickComplete(trick)) {
            finishTrick(trick)
        }
    }

    private fun findTrickEntity(
        trickNumber: Int,
        roundNumber: Int,
        gameUid: String
    ) = TrickEntity.findByNumber(trickNumber, roundNumber, gameUid)
        ?: throw TrickNotFoundException("Could not find trick $trickNumber for round $roundNumber of game $gameUid")

    private fun ensureMoveIsValid(move: Move, trick: TrickEntity, player: PlayerEntity) {
        val playedCards = trick.moves
            .map { it.card.toModel() }
        val playerHand = player.playerHands
            .single { it.round == trick.round }
            .hand
            .toModel()
        val trump = trick.round.trump!!

        val evaluationResult = evaluator.evaluate(playedCards, playerHand, move.card, trump)
        if (!(evaluationResult == MoveEvaluationResult.WINS || evaluationResult == MoveEvaluationResult.LOOSES)) {
            throw IllegalMoveException("Playing card ${move.card} is not a valid move (reason: $evaluationResult)")
        }
    }

    private fun persistMove(move: Move, trick: TrickEntity, player: PlayerEntity) {
        val moveEntity = MoveEntity()
        moveEntity.trick = trick
        trick.moves.add(moveEntity)
        moveEntity.number = trick.moves.size
        moveEntity.card = createCardEntity(move.card)
        moveEntity.card.move = moveEntity
        moveEntity.player = player
        moveEntity.persist()
    }

    private fun createCardEntity(card: Card): CardEntity {
        val cardEntity = CardEntity()
        cardEntity.rank = card.rank
        cardEntity.suit = card.suit
        return cardEntity
    }

    private fun removePlayerFromTrick(player: PlayerEntity, trick: TrickEntity) {
        trick.pendingPlayers.remove(player)
        trick.persist()
    }

    private fun isTrickComplete(trick: TrickEntity) = trick.pendingPlayers.isEmpty()

    private fun finishTrick(trick: TrickEntity) {
        trickService.finishTrick(trick.round.toModel(), trick.round.game.toModel())
    }
}
