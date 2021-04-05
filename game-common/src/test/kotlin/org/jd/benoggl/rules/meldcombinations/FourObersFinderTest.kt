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
internal class FourObersFinderTest {

    val sut = FourObersFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        combinations.shouldBeEmpty()
    }

    @Test
    fun oneCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.LEAVES, Rank.OBER)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 1
        combinations.first().type shouldBe MeldCombinationType.FOUR_OBERS
        combinations.first().points shouldBe 60
    }

    @Test
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.LEAVES, Rank.OBER),
                Card(Suit.LEAVES, Rank.OBER)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 2
    }

    @Test
    fun noCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.HEARTS, Rank.OBER)
            ),
            Suit.ACORNS
        )

        combinations.shouldBeEmpty()
    }

}