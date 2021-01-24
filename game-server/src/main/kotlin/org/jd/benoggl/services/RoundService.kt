package org.jd.benoggl.services

import org.jd.benoggl.entities.*
import org.jd.benoggl.models.*
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
@Transactional
class RoundService {

    @Inject
    @field: Default
    internal lateinit var cardService: CardService

    fun startNewRound(game: Game) {
        check(game.state == GameState.RUNNING)

        val gameEntity = GameEntity.findByUid(game.uid)!!

        val round = createRound(gameEntity, game)
        gameEntity.rounds.add(round)
        round.bidding = createBidding(gameEntity)
        dealCards(game, gameEntity, round)
        round.persist()
    }

    private fun dealCards(
        game: Game,
        gameEntity: GameEntity,
        round: RoundEntity
    ) {
        val dealtCards = cardService.dealCards(game.players.size)
        round.dabb = toEntity(dealtCards.dabb)
        round.playerHands = game.players.mapIndexed { i, player ->
            val hand = dealtCards.playerHands[i]
            createPlayerHand(gameEntity, round, player, hand)
        }.toMutableList()
    }

    private fun createPlayerHand(game: GameEntity, round: RoundEntity, player: Player, hand: Hand): PlayerHandEntity {
        val playerHand = PlayerHandEntity()
        playerHand.player = game.players.find { it.uid == player.uid }!!
        playerHand.hand = toEntity(hand)
        playerHand.round = round
        return playerHand
    }

    private fun toEntity(model: Hand): HandEntity {
        val entity = HandEntity()
        entity.type = model.type
        entity.cards = model.cards.map { it ->
            val card = CardEntity()
            card.suit = it.suit
            card.rank = it.rank
            card.hand = entity
            card
        }.toMutableList()
        return entity
    }


    private fun createRound(
        gameEntity: GameEntity,
        game: Game
    ): RoundEntity {
        val round = RoundEntity()
        round.game = gameEntity
        round.state = RoundState.BIDDING
        round.type = RoundType.NORMAL
        round.number = game.rounds.size
        return round
    }

    private fun createBidding(gameEntity: GameEntity): BiddingEntity {
        val bidding = BiddingEntity()
        bidding.state = BiddingState.RUNNING
        bidding.challengers = emptyList<PlayerEntity>().toMutableList()
        gameEntity.players.forEach { bidding.challengers.add(it) }
        return bidding
    }

}