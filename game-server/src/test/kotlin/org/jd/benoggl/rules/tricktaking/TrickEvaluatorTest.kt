package org.jd.benoggl.rules.tricktaking

import io.quarkus.test.junit.QuarkusTest
import org.hamcrest.MatcherAssert.assertThat
import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Hand
import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import org.junit.jupiter.api.Test
import org.hamcrest.Matchers.`is` as Is

@QuarkusTest
class TrickEvaluatorTest {

    private val heartUnter = Card(Suit.HEARTS, Rank.UNTER)
    private val heartOber = Card(Suit.HEARTS, Rank.OBER)
    private val heartKing = Card(Suit.HEARTS, Rank.KING)
    private val leavesUnter = Card(Suit.LEAVES, Rank.UNTER)
    private val leavesOber = Card(Suit.LEAVES, Rank.OBER)
    private val leavesKing = Card(Suit.LEAVES, Rank.KING)
    private val bellsKing = Card(Suit.BELLS, Rank.KING)

    private val sut = TrickEvaluator()

    @Test
    fun higherRankBeatsLowerRank() {
        val playedCards = listOf(
            heartUnter,
            heartOber
        )
        val playerHand = handOf(heartKing)

        val result = sut.evaluate(playedCards, playerHand, heartKing, Suit.LEAVES)

        assertThat(result, Is(TrickEvaluationResult.WINS))
    }

    @Test
    fun higherTrumpRankBeatsLowerTrumpRank() {
        val playedCards = listOf(
            heartUnter,
            heartOber
        )
        val playerHand = handOf(heartKing)

        val result = sut.evaluate(playedCards, playerHand, heartKing, Suit.HEARTS)

        assertThat(result, Is(TrickEvaluationResult.WINS))
    }

    @Test
    fun lowerRankLoosesToHigherRank() {
        val playedCards = listOf(
            heartOber,
            heartKing
        )
        val playerHand = handOf(heartUnter)

        val result = sut.evaluate(playedCards, playerHand, heartUnter, Suit.LEAVES)

        assertThat(result, Is(TrickEvaluationResult.LOOSES))
    }

    @Test
    fun lowerTrumpRankLoosesToHigherTrumpRank() {
        val playedCards = listOf(
            heartOber,
            heartKing
        )
        val playerHand = handOf(heartUnter)

        val result = sut.evaluate(playedCards, playerHand, heartUnter, Suit.HEARTS)

        assertThat(result, Is(TrickEvaluationResult.LOOSES))
    }

    @Test
    fun lowerTrumpRankBeatsHigherNonTrumpRank() {
        val playedCards = listOf(
            leavesKing,
            leavesOber
        )
        val playerHand = handOf(heartUnter)

        val result = sut.evaluate(playedCards, playerHand, heartUnter, Suit.HEARTS)

        assertThat(result, Is(TrickEvaluationResult.WINS))
    }

    @Test
    fun higherNonTrumpRankLoosesToLowerTrumpRank() {
        val playedCards = listOf(
            heartUnter,
            heartOber
        )
        val playerHand = handOf(leavesKing)

        val result = sut.evaluate(playedCards, playerHand, leavesKing, Suit.HEARTS)

        assertThat(result, Is(TrickEvaluationResult.LOOSES))
    }

    @Test
    fun sameCardLooses() {
        val playedCards = listOf(
            leavesUnter,
            leavesOber
        )
        val playerHand = handOf(leavesOber)

        val result = sut.evaluate(playedCards, playerHand, leavesOber, Suit.HEARTS)

        assertThat(result, Is(TrickEvaluationResult.LOOSES))
    }

    @Test
    fun sameTrumpCardLooses() {
        val playedCards = listOf(
            leavesUnter,
            leavesOber
        )
        val playerHand = handOf(leavesOber)

        val result = sut.evaluate(playedCards, playerHand, leavesOber, Suit.LEAVES)

        assertThat(result, Is(TrickEvaluationResult.LOOSES))
    }

    @Test
    fun mustFollowSuit() {
        val playedCards = listOf(
            leavesUnter,
            leavesOber
        )
        val playerHand = handOf(leavesUnter, bellsKing)

        val result = sut.evaluate(playedCards, playerHand, bellsKing, Suit.HEARTS)

        assertThat(result, Is(TrickEvaluationResult.SUIT_NOT_FOLLOWED))
    }

    @Test
    fun mustFollowSuitEvenIfTrumpOnHand() {
        val playedCards = listOf(
            leavesUnter,
            leavesOber
        )
        val playerHand = handOf(leavesUnter, heartKing)

        val result = sut.evaluate(playedCards, playerHand, heartKing, Suit.HEARTS)

        assertThat(result, Is(TrickEvaluationResult.SUIT_NOT_FOLLOWED))
    }

    @Test
    fun mustPlayTrumpIfSuitCantBeFollowed() {
        val playedCards = listOf(
            leavesUnter,
            leavesOber
        )
        val playerHand = handOf(bellsKing, heartOber)

        val result = sut.evaluate(playedCards, playerHand, bellsKing, Suit.HEARTS)

        assertThat(result, Is(TrickEvaluationResult.TRUMP_NOT_PLAYED))
    }

    @Test
    fun canIgnoreSuitAndTrumpIfNotOnHand() {
        val playedCards = listOf(
            leavesUnter,
            leavesOber
        )
        val playerHand = handOf(bellsKing)

        val result = sut.evaluate(playedCards, playerHand, bellsKing, Suit.HEARTS)

        assertThat(result, Is(TrickEvaluationResult.LOOSES))
    }

    @Test
    fun mustPlayWinningCard() {
        val playedCards = listOf(
            leavesUnter,
            leavesOber
        )
        val playerHand = handOf(leavesUnter, leavesKing)

        val result = sut.evaluate(playedCards, playerHand, leavesUnter, Suit.HEARTS)

        assertThat(result, Is(TrickEvaluationResult.WINNING_CARD_NOT_PLAYED))
    }

    @Test
    fun mustPlayWinningCardNotDraw() {
        val playedCards = listOf(
            leavesUnter,
            leavesOber
        )
        val playerHand = handOf(leavesOber, leavesKing)

        val result = sut.evaluate(playedCards, playerHand, leavesOber, Suit.HEARTS)

        assertThat(result, Is(TrickEvaluationResult.WINNING_CARD_NOT_PLAYED))
    }

    @Test
    fun mustNotPlaySameCardIfSuitMatchedAndNoWinningCardAvailable() {
        val playedCards = listOf(
            leavesUnter,
            leavesOber
        )
        val playerHand = handOf(leavesUnter, leavesOber)

        val result = sut.evaluate(playedCards, playerHand, leavesUnter, Suit.HEARTS)

        assertThat(result, Is(TrickEvaluationResult.LOOSES))
    }

    private fun handOf(vararg cards: Card) = Hand(cards.toMutableList())

}