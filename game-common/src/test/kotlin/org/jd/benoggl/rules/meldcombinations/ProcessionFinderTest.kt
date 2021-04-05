package org.jd.benoggl.rules.meldcombinations

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.quarkus.test.junit.QuarkusTest
import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import org.junit.jupiter.api.Test

@QuarkusTest
internal class ProcessionFinderTest {


    val sut = ProcessionFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        combinations.shouldBeEmpty()
    }

    @Test
    fun noProcession() {
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

    @Test
    fun oneProcession() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.LEAVES, Rank.KING),
                Card(Suit.LEAVES, Rank.OBER)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 1
        combinations.first().type shouldBe MeldCombinationType.PROCESSION
        combinations.first().points shouldBe 240
        combinations.first().suit.shouldBeNull()
        combinations.first().blockedCombinations.shouldContainExactlyInAnyOrder(
            BlockedMeldCombination(MeldCombinationType.TRUMP_PAIR, Suit.ACORNS),
            BlockedMeldCombination(MeldCombinationType.PAIR, Suit.BELLS),
            BlockedMeldCombination(MeldCombinationType.PAIR, Suit.HEARTS),
            BlockedMeldCombination(MeldCombinationType.PAIR, Suit.LEAVES)
        )
    }

    @Test
    fun twoProcessions() {
        val combinations = sut.findCombinations(
            listOf(
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.ACORNS, Rank.OBER),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.LEAVES, Rank.KING),
                Card(Suit.LEAVES, Rank.KING),
                Card(Suit.LEAVES, Rank.OBER),
                Card(Suit.LEAVES, Rank.OBER)
            ),
            Suit.ACORNS
        )

        combinations shouldHaveSize 2
    }

}