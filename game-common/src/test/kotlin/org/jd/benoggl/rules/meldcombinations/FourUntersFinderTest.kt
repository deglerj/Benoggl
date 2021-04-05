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
internal class FourUntersFinderTest {

    val sut = FourUntersFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        combinations.shouldBeEmpty()
    }

    @Test
    fun oneCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.UNTER),
                Card(Suit.LEAVES, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 1
        combinations.first().type shouldBe MeldCombinationType.FOUR_UNTERS
        combinations.first().points shouldBe 40
    }

    @Test
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.UNTER),
                Card(Suit.ACORNS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.UNTER),
                Card(Suit.LEAVES, Rank.UNTER),
                Card(Suit.LEAVES, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 2
    }

    @Test
    fun noCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        combinations.shouldBeEmpty()
    }

}