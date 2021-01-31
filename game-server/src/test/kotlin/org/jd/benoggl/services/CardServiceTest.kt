package org.jd.benoggl.services

import io.quarkus.test.junit.QuarkusTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.jd.benoggl.models.HandType
import org.junit.jupiter.api.Test
import javax.enterprise.inject.Default
import javax.inject.Inject
import org.hamcrest.Matchers.`is` as Is

@QuarkusTest
class CardServiceTest {

    @Inject
    @field: Default
    internal lateinit var sut: CardService

    @Test
    fun dealCards() {
        val dealt = sut.dealCards(2)

        assertThat(dealt.dabb.cards, hasSize(4))
        assertThat(dealt.dabb.type, Is(HandType.DABB))
        assertThat(dealt.playerHands, hasSize(2))
        assertThat(dealt.playerHands[0].cards, hasSize((40 - 4) / 2))
        assertThat(dealt.playerHands[0].type, Is(HandType.NORMAL))
        assertThat(dealt.playerHands[1].cards, hasSize((40 - 4) / 2))
        assertThat(dealt.playerHands[1].type, Is(HandType.NORMAL))
    }
}