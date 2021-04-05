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
internal class TrumpFamilyFinderTest {

    val sut = TrumpFamilyFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        combinations.shouldBeEmpty()
    }

    @Test
    fun oneFamily() {
        val combinations = sut.findCombinations(
            createFamily(Suit.ACORNS),
            Suit.ACORNS
        )

        combinations shouldHaveSize 1
        combinations.first().type shouldBe MeldCombinationType.TRUMP_FAMILY
        combinations.first().points shouldBe 150
    }

    @Test
    fun twoFamilies() {
        val combinations = sut.findCombinations(
            createFamily(Suit.BELLS)
                    + createFamily(Suit.BELLS),
            Suit.BELLS
        )

        combinations shouldHaveSize 2
    }

    @Test
    fun oneFamilyAndOneIncompleteFamilyOfSameSuit() {
        val combinations = sut.findCombinations(
            createFamily(Suit.ACORNS)
                    + listOf(
                Card(Suit.ACORNS, Rank.ACE),
                Card(Suit.ACORNS, Rank.TEN),
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
                Card(Suit.ACORNS, Rank.KING),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.ACORNS, Rank.ACE)
            ),
            Suit.ACORNS
        )

        combinations.shouldBeEmpty()
    }

    private fun createFamily(suit: Suit): Collection<Card> =
        listOf(
            Card(suit, Rank.ACE),
            Card(suit, Rank.TEN),
            Card(suit, Rank.KING),
            Card(suit, Rank.OBER),
            Card(suit, Rank.UNTER)
        )


}