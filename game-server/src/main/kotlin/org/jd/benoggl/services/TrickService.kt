package org.jd.benoggl.services

import org.jd.benoggl.entities.CardEntity
import org.jd.benoggl.entities.MoveEntity
import org.jd.benoggl.entities.RoundEntity
import org.jd.benoggl.entities.TrickEntity
import org.jd.benoggl.models.*
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
        check(round.state == RoundState.DISCARDING) { "Cant start first trick for round ${round.number} of game ${game.uid}. Round is invalid state ${round.state}" }

        val roundEntity = findRoundEntity(round, game)
        roundEntity.state = RoundState.TRICKING
        roundEntity.tricks.add(createTrick(roundEntity))
        roundEntity.persist()
    }

    private fun createTrick(round: RoundEntity): TrickEntity {
        val trick = TrickEntity()
        trick.number = round.tricks.size
        trick.state = TrickState.RUNNING
        trick.pendingPlayers = round.game.players.toMutableList()
        trick.round = round
        return trick
    }

    private fun findRoundEntity(
        round: Round,
        game: Game
    ) = (RoundEntity.findByNumber(round.number, game.uid)
        ?: throw IllegalStateException("Round ${round.number} of game ${game.uid} not present in database"))

    private fun isFirstTrick(round: Round) = round.tricks.isEmpty()

    fun finishTrick(round: Round, game: Game) {
        val roundEntity = findRoundEntity(round, game)

        finishTrick(roundEntity.tricks.last())

        if (isLastTrickInRound(roundEntity)) {
            roundService.finishRound(round, game)
        } else {
            startNewTrick(roundEntity)
        }
    }

    private fun finishTrick(trick: TrickEntity) {
        val move = findWinningMove(trick)
        trick.winner = move.player
        trick.state = TrickState.FINISHED
        trick.persist()
    }

    private fun findWinningMove(trick: TrickEntity): MoveEntity {
        val trump = trick.round.trump!!
        val moves = trick.moves.toMutableList()
        var winningMove = moves.removeFirst()
        moves.forEach { move ->
            if (isWinningCard(move.card, winningMove.card, trump)) {
                winningMove = move
            }
        }
        return winningMove
    }

    private fun isWinningCard(cardToPlay: CardEntity, playedCard: CardEntity, trump: Suit): Boolean {
        val trumpPlayed = playedCard.suit == trump
        val trumpToPlay = cardToPlay.suit == trump
        val matchesSuit = playedCard.suit == cardToPlay.suit

        return when {
            trumpPlayed && !trumpToPlay -> false // Non-trump always looses to trump
            !trumpPlayed && trumpToPlay -> true // Trump always wins over non-trump
            !matchesSuit -> false // Not following suit always looses
            else -> cardToPlay.rank > playedCard.rank // Else higher rank wins
        }
    }

    private fun startNewTrick(roundEntity: RoundEntity) {
        roundEntity.tricks.add(createTrick(roundEntity))
        roundEntity.persist()
    }

    private fun isLastTrickInRound(roundEntity: RoundEntity): Boolean =
        roundEntity.playerHands
            .map { playerHand -> playerHand.hand.cards }
            .all { cards -> cards.isEmpty() }
}