package org.jd.benoggl.models.meldcombinations

import org.jd.benoggl.models.Card
import org.jd.benoggl.models.Suit
import org.jd.benoggl.removeFirst

class MeldCombinationsInCardsFinder {

    private val finders = listOf(
        FamilyFinder(),
        TrumpFamilyFinder(),
        PairFinder(),
        TrumpPairFinder(),
        ProcessionFinder()
    )

    fun findCombinations(cards: Collection<Card>, trump: Suit): Collection<MeldCombination> {
        val possibleCombinations = finders.flatMap { it.findCombinations(cards, trump) }
        return removeBlockedCombinations(possibleCombinations)
    }

    private fun removeBlockedCombinations(possibleCombinations: List<MeldCombination>): Collection<MeldCombination> {
        val blockedCombinations = possibleCombinations.flatMap(MeldCombination::blockedCombinations)

        val withoutBlockedCombinations = possibleCombinations.toMutableList()
        blockedCombinations.forEach { blockedCombination ->
            withoutBlockedCombinations.removeFirst(blockedCombination::isBlocking)
        }
        return withoutBlockedCombinations
    }

}