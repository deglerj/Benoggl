package org.jd.benoggl.services

import org.jd.benoggl.model.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CardService {

    fun dealCards(playerCount: Int): DealtCards {
        check(playerCount >= 2)

        val deck = createDeck().shuffled().toMutableList()
        val dabb = takeDabbCards(deck)
        val playerHands = splitDeckBetweenPlayers(deck, playerCount)

        return DealtCards(playerHands, dabb)
    }

    private fun takeDabbCards(deck: MutableList<Card>): Hand {
        val cards = (1..4).map { deck.removeAt(0) }
        return Hand(cards.toMutableList(), HandType.DABB)
    }

    private fun splitDeckBetweenPlayers(deck: MutableList<Card>, playerCount: Int): List<Hand> {
        val cardsPerPlayer = deck.size / playerCount
        return deck.chunked(cardsPerPlayer) { Hand(it.toMutableList(), HandType.NORMAL) }
    }


    private fun createDeck(): List<Card> {
        return Suit.values().flatMap { suit ->
            Rank.values().flatMap { rank ->
                listOf(
                    Card(suit, rank),
                    Card(suit, rank)
                )
            }
        }
    }
}