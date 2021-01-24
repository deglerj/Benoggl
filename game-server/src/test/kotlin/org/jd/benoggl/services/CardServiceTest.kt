package org.jd.benoggl.services

import io.quarkus.test.junit.QuarkusTest
import org.jd.benoggl.models.HandType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.enterprise.inject.Default
import javax.inject.Inject

@QuarkusTest
class CardServiceTest {

    @Inject
    @field: Default
    internal lateinit var sut: CardService

    @Test
    fun dealCards() {
        val dealt = sut.dealCards(2)

        assertEquals(4, dealt.dabb.cards.size)
        assertEquals(HandType.DABB, dealt.dabb.type)
        assertEquals(2, dealt.playerHands.size)
        assertEquals((40 - 4) / 2, dealt.playerHands[0].cards.size)
        assertEquals(HandType.NORMAL, dealt.playerHands[0].type)
    }
}