package org.jd.benoggl.common.events

import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jd.benoggl.common.models.Rank
import org.jd.benoggl.common.models.Suit
import org.junit.jupiter.api.Test

internal class CardsDealtEventTest {

    private val json = Json

    @Test
    fun constructor_newInstance_hasFullDeck() {
        val sut = CardsDealtEvent()

        sut.deck shouldHaveSize Suit.values().size * Rank.values().size * 2
    }

    @Test
    fun constructor_deserializedInstance_shouldHaveSerializedDeck() {
        val sut = CardsDealtEvent()

        val serialized = json.encodeToString(sut)
        val deserialized = json.decodeFromString<CardsDealtEvent>(serialized)

        deserialized.deck shouldContainInOrder sut.deck
    }

}