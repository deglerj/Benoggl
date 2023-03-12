package org.jd.benoggl.common.rules.melds

import org.jd.benoggl.common.models.Card
import org.jd.benoggl.common.models.MeldCombination
import org.jd.benoggl.common.models.Suit
import org.jd.benoggl.common.removeFirst

class MeldCombinationsInCardsFinder {

    private val finders = listOf(
        FamilyFinder(),
        TrumpFamilyFinder(),
        PairFinder(),
        TrumpPairFinder(),
        ProcessionFinder(),
        FourUntersFinder(),
        FourObersFinder(),
        FourKingsFinder(),
        FourAcesFinder(),
        BinokelFinder(),
        DoubleBinokelFinder()
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