package org.jd.benoggl.models.meldcombinations

data class MeldCombination(
    val name: String,
    val points: Int,
    val blockedCombinations: Collection<MeldCombination> = emptyList()
) {

}