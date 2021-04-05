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
internal class FourAcesFinderTest {

    val sut = FourAcesFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        combinations.shouldBeEmpty()
    }

    @Test
    fun oneCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.ACE),
                Card(Suit.BELLS, Rank.ACE),
                Card(Suit.HEARTS, Rank.ACE),
                Card(Suit.LEAVES, Rank.ACE)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 1
        combinations.first().type shouldBe MeldCombinationType.FOUR_ACES
        combinations.first().points shouldBe 100
    }

    @Test
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.ACE),
                Card(Suit.ACORNS, Rank.ACE),
                Card(Suit.BELLS, Rank.ACE),
                Card(Suit.BELLS, Rank.ACE),
                Card(Suit.HEARTS, Rank.ACE),
                Card(Suit.HEARTS, Rank.ACE),
                Card(Suit.LEAVES, Rank.ACE),
                Card(Suit.LEAVES, Rank.ACE)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 2
    }

    @Test
    fun noCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.ACE),
                Card(Suit.BELLS, Rank.ACE),
                Card(Suit.HEARTS, Rank.ACE)
            ),
            Suit.ACORNS
        )

        combinations.shouldBeEmpty()
    }

}