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
internal class FourKingsFinderTest {

    val sut = FourKingsFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        combinations.shouldBeEmpty()
    }

    @Test
    fun oneCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.LEAVES, Rank.KING)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 1
        combinations.first().type shouldBe MeldCombinationType.FOUR_KINGS
        combinations.first().points shouldBe 80
    }

    @Test
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.LEAVES, Rank.KING),
                Card(Suit.LEAVES, Rank.KING)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 2
    }

    @Test
    fun noCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING)
            ),
            Suit.ACORNS
        )

        combinations.shouldBeEmpty()
    }

}