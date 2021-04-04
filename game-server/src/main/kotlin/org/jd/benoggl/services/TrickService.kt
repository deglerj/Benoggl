package org.jd.benoggl.services

import org.jd.benoggl.entities.RoundEntity
import org.jd.benoggl.entities.TrickEntity
import org.jd.benoggl.models.Game
import org.jd.benoggl.models.Round
import org.jd.benoggl.models.RoundState
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
@Transactional
class TrickService {

    @Inject
    @field: Default
    internal lateinit var roundService: RoundService

    fun startFirstTrick(round: Round, game: Game) {
        check(isFirstTrick(round)) { "Cant start first trick for round ${round.number} of game ${game.uid}. Already has one or more tricks." }
        check(round.state == RoundState.MELDING) { "Cant start first trick for round ${round.number} of game ${game.uid}. Round is invalid state ${round.state}" }

        val roundEntity = findRoundEntity(round, game)
        roundEntity.state = RoundState.TRICKING
        roundEntity.tricks.add(TrickEntity())
        roundEntity.persist()
    }

    private fun findRoundEntity(
        round: Round,
        game: Game
    ) = (RoundEntity.findByNumber(round.number, game.uid)
        ?: throw IllegalStateException("Round ${round.number} of game ${game.uid} not present in database"))

    private fun isFirstTrick(round: Round) = round.tricks.isEmpty()

    fun finishTrick(round: Round, game: Game) {
        val roundEntity = findRoundEntity(round, game)

        if (isLastTrickInRound(roundEntity)) {
            roundService.finishRound(round, game)
        } else {
            startNewTrick(roundEntity)
        }
    }

    private fun startNewTrick(roundEntity: RoundEntity) {
        roundEntity.tricks.add(TrickEntity())
        roundEntity.persist()
    }

    private fun isLastTrickInRound(roundEntity: RoundEntity): Boolean =
        roundEntity.playerHands
            .map { playerHand -> playerHand.hand.cards }
            .all { cards -> cards.isEmpty() }
}