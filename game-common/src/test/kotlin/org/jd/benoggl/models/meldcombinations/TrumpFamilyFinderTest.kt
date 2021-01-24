package org.jd.benoggl.models.meldcombinations

import io.quarkus.test.junit.QuarkusTest
import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@QuarkusTest
internal class TrumpFamilyFinderTest {

    val sut = TrumpFamilyFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        Assertions.assertTrue(combinations.isEmpty())
    }

    @Test
    fun oneFamily() {
        val combinations = sut.findCombinations(
            createFamily(Suit.ACORNS),
            Suit.ACORNS
        )

        Assertions.assertEquals(1, combinations.size)
        Assertions.assertEquals(150, combinations.first().points)
    }

    @Test
    fun twoFamilies() {
        val combinations = sut.findCombinations(
            createFamily(Suit.ACORNS)
                    + createFamily(Suit.ACORNS),
            Suit.ACORNS
        )

        Assertions.assertEquals(2, combinations.size)
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

        Assertions.assertEquals(1, combinations.size)
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

        Assertions.assertTrue(combinations.isEmpty())
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