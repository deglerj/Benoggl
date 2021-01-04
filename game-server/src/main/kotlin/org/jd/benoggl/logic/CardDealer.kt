package org.jd.benoggl.logic

import org.jd.benoggl.model.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CardDealer {

    fun dealCards(playerCount: Int): DealtCards {
        check(playerCount >= 2)

        // TODO implement
        val card = Card(Suit.ACORNS, Rank.ACE)
        val playerHands = mutableListOf<Hand>()
        repeat(playerCount) {
            playerHands.add(Hand(mutableListOf(card), HandType.NORMAL))
        }
        return DealtCards(
            playerHands,
            Hand(mutableListOf(card), HandType.DABB)
        )
    }

}