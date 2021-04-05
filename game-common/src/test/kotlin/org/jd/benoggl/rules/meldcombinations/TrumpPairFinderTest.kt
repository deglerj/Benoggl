package org.jd.benoggl.rules.meldcombinations

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.quarkus.test.junit.QuarkusTest
import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import org.junit.jupiter.api.Test

@QuarkusTest
internal class TrumpPairFinderTest {

    val sut = TrumpPairFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        combinations.shouldBeEmpty()
    }

    @Test
    fun onePair() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.OBER)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 1
        combinations.first().type shouldBe MeldCombinationType.TRUMP_PAIR
        combinations.first().points shouldBe 40
    }

    @Test
    fun twoPairs() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.OBER)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 2
    }

    @Test
    fun onePairAndOneIncompletePairOfSameSuit() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.ACORNS, Rank.KING)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 1
    }

    @Test
    fun noPairs() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.ACE)
            ),
            Suit.ACORNS
        )

        combinations.shouldBeEmpty()
    }

}