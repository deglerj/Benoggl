package org.jd.benoggl.common.events

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jd.benoggl.common.models.Rank
import org.jd.benoggl.common.models.Suit
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class CardsDealtEventTest {

    private val json = Json

    @Test
    fun constructor_newInstance_hasFullDeck() {
        val sut = CardsDealtEvent()

        assertEquals(Suit.values().size * Rank.values().size * 2, sut.deck.size)
    }

    @Test
    fun constructor_deserializedInstance_shouldHaveSerializedDeck() {
        val sut = CardsDealtEvent()

        val serialized = json.encodeToString(sut)
        val deserialized = json.decodeFromString<CardsDealtEvent>(serialized)

        assertContentEquals(sut.deck, deserialized.deck)
    }

}