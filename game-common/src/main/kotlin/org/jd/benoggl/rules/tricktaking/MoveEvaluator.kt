package org.jd.benoggl.rules.tricktaking

import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Hand
import org.jd.benoggl.models.Suit
import org.jd.benoggl.models.findSuits

class MoveEvaluator {

    fun evaluate(playedCards: List<Card>, playerHand: Hand, cardToPlay: Card, trump: Suit): MoveEvaluationResult {
        // Follow suit
        val firstPlayedCard = playedCards.firstOrNull()
        val playerHasSuit =
            if (firstPlayedCard == null) true else hasCardWithSuit(playerHand.cards, firstPlayedCard.suit)
        val cardToPlayMatchesSuit =
            if (firstPlayedCard == null) true else firstPlayedCard.suit == cardToPlay.suit
        if (playerHasSuit && !cardToPlayMatchesSuit) {
            return MoveEvaluationResult.SUIT_NOT_FOLLOWED
        }

        // Must play trump if suit can't be followed
        val playerHasTrump = playerHand.cards.findSuits(trump).any()
        val cardToPlayIsTrump = cardToPlay.suit == trump
        if (!playerHasSuit && playerHasTrump && !cardToPlayIsTrump) {
            return MoveEvaluationResult.TRUMP_NOT_PLAYED
        }

        val cardToBeat = findWinningCard(playedCards, trump) ?: return MoveEvaluationResult.WINS

        // Must play winning card
        val winningCardsOnHand = findWinningCardsOnHand(playerHand.cards, cardToBeat, trump)
        val playerHasWinningCard = winningCardsOnHand.isNotEmpty()
        val playerHasOnlyWinningTrumpCards = winningCardsOnHand.all { card -> card.suit == trump }
        val playerMustFollowSuitInsteadOfWin = playerHasSuit && playerHasOnlyWinningTrumpCards
        val cardToPlayIsWinning = isCardBeatingOtherCard(cardToPlay, cardToBeat, trump)
        if (playerHasWinningCard && !cardToPlayIsWinning && !playerMustFollowSuitInsteadOfWin) {
            return MoveEvaluationResult.WINNING_CARD_NOT_PLAYED
        }

        return if (cardToPlayIsWinning) MoveEvaluationResult.WINS else MoveEvaluationResult.LOOSES
    }

    private fun findWinningCard(playedCards: List<Card>, trump: Suit) =
        playedCards
            .sortedWith { card1, card2 ->
                if (isCardBeatingOtherCard(card1, card2, trump)) {
                    -1
                } else {
                    1
                }
            }
            .firstOrNull()

    private fun hasCardWithSuit(cards: MutableCollection<Card>, suit: Suit) =
        cards.findSuits(suit).any()

    private fun findWinningCardsOnHand(cards: Collection<Card>, cardToBeat: Card, trump: Suit) =
        cards.filter { card -> isCardBeatingOtherCard(card, cardToBeat, trump) }

    private fun isCardBeatingOtherCard(cardToPlay: Card, cardToBeat: Card, trump: Suit): Boolean {
        // Trump always wins
        if (cardToPlay.suit == trump && cardToBeat.suit != trump) {
            return true
        }

        // Different suit always looses
        if (cardToBeat.suit != cardToPlay.suit) {
            return false
        }

        // Same suit? → Higher rank wins
        return cardToPlay.rank.points > cardToBeat.rank.points
    }

}