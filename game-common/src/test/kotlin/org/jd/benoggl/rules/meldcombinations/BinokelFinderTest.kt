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
internal class BinokelFinderTest {

    val sut = BinokelFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        combinations.shouldBeEmpty()
    }

    @Test
    fun oneCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.LEAVES, Rank.OBER),
                Card(Suit.BELLS, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 1
        combinations.first().type shouldBe MeldCombinationType.BINOKEL
        combinations.first().points shouldBe 40
    }

    @Test
    fun twoCombinations() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.LEAVES, Rank.OBER),
                Card(Suit.LEAVES, Rank.OBER),
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.BELLS, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        combinations.shouldBeEmpty()
    }

    @Test
    fun noCombination() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.LEAVES, Rank.OBER),
                Card(Suit.LEAVES, Rank.UNTER)
            ),
            Suit.ACORNS
        )

        combinations.shouldBeEmpty()
    }

}