package org.jd.benoggl.models.meldcombinations

import io.quarkus.test.junit.QuarkusTest
import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Rank
import org.jd.benoggl.models.Suit
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@QuarkusTest
internal class FamilyFinderTest {

    val sut = FamilyFinder()

    @Test
    fun noCards() {
        val combinations = sut.findCombinations(emptyList(), Suit.ACORNS)

        Assertions.assertTrue(combinations.isEmpty())
    }

    @Test
    fun oneFamily() {
        val combinations = sut.findCombinations(
            createFamily(Suit.BELLS),
            Suit.ACORNS
        )

        Assertions.assertEquals(1, combinations.size)
        Assertions.assertEquals(100, combinations.first().points)
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

        Assertions.assertEquals(3, combinations.size)
    }

    @Test
    fun twoFamiliesOfSameSuit() {
        val combinations = sut.findCombinations(
            createFamily(Suit.BELLS)
                    + createFamily(Suit.BELLS),
            Suit.ACORNS
        )

        Assertions.assertEquals(2, combinations.size)
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

        Assertions.assertEquals(1, combinations.size)
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