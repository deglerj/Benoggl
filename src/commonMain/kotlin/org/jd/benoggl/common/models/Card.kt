package org.jd.benoggl.common.models

import kotlinx.serialization.Serializable

@Serializable
data class Card(val suit: Suit, val rank: Rank) {

    companion object {
        fun createDeck(): List<Card> {
            return Suit.values().flatMap { suit ->
                Rank.values().flatMap { rank ->
                    listOf(
                        Card(suit, rank),
                        Card(suit, rank)
                    )
                }
            }
        }
    }

    override fun toString(): String {
        return "$suit-$rank"
    }

}
