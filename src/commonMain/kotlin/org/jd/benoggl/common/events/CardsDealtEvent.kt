package org.jd.benoggl.common.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jd.benoggl.common.models.*

@Serializable
@SerialName("cards-dealt")
data class CardsDealtEvent(val deck: List<Card> = Card.createDeck().shuffled()) : Event {

    override fun apply(game: Game) {
        // This event only spawns child events to deal cards to players and place the dabb
    }

    override fun createChildEvents(game: Game): List<Event> {
        val remainingDeck = deck.toMutableList()
        val dabbPlaced = DabbPlacedEvent(takeDabbCards(remainingDeck))
        val cardsDealt = dealCardsToPlayers(remainingDeck, game.players)

        return cardsDealt + dabbPlaced
    }

    private fun dealCardsToPlayers(remainingDeck: MutableList<Card>, players: List<Player>): List<HandDealtEvent> {
        val playerCount = players.size
        check(playerCount >= 2)

        val cardsPerPlayer = remainingDeck.size / playerCount
        val chunks = remainingDeck.chunked(cardsPerPlayer) { Hand(it.toMutableList()) }
        return chunks.mapIndexed { i, hand -> HandDealtEvent(hand, players[i].uid) }
    }

    private fun takeDabbCards(remainingDeck: MutableList<Card>): Dabb {
        val cards = (1..4).map { remainingDeck.removeAt(0) }
        return Dabb(cards)
    }

    override fun isVisibleForPlayer(game: Game, player: Player) = false

    override fun explain(game: Game) = "Shuffled cards"

}