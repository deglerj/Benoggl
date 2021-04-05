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
internal class FamilyFinderTest {

    val sut = FamilyFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        combinations.shouldBeEmpty()
    }

    @Test
    fun oneFamily() {
        val combinations = sut.findCombinations(
            createFamily(Suit.BELLS),
            Suit.ACORNS
        )

        combinations shouldHaveSize 1
        combinations.first().points shouldBe 100
    }

    @Test
    fun oneFamilyOfEach() {
        val combinations = sut.findCombinations(
            createFamily(Suit.ACORNS)
                    + createFamily(Suit.BELLS)
                    + createFamily(Suit.HEARTS)
                    + createFamily(Suit.LEAVES),
            Suit.ACORNS
        )

        combinations shouldHaveSize 3
    }

    @Test
    fun twoFamiliesOfSameSuit() {
        val combinations = sut.findCombinations(
            createFamily(Suit.BELLS)
                    + createFamily(Suit.BELLS),
            Suit.ACORNS
        )

        combinations shouldHaveSize 2
    }

    @Test
    fun oneFamilyAndOneIncompleteFamilyOfSameSuit() {
        val combinations = sut.findCombinations(
            createFamily(Suit.BELLS)
                    + listOf(
                Card(Suit.BELLS, Rank.ACE),
                Card(Suit.BELLS, Rank.TEN),
                Card(Suit.BELLS, Rank.KING)
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

    private fun createFamily(suit: Suit): Collection<Card> =
        listOf(
            Card(suit, Rank.ACE),
            Card(suit, Rank.TEN),
            Card(suit, Rank.KING),
            Card(suit, Rank.OBER),
            Card(suit, Rank.UNTER)
        )


}