package org.jd.benoggl.services

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.quarkus.test.junit.QuarkusTest
import org.jd.benoggl.models.HandType
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

        dealt.dabb.cards shouldHaveSize 4
        dealt.dabb.type shouldBe HandType.DABB
        dealt.playerHands shouldHaveSize 2
        dealt.playerHands[0].cards shouldHaveSize ((40 - 4) / 2)
        dealt.playerHands[0].type shouldBe HandType.NORMAL
        dealt.playerHands[1].cards shouldHaveSize ((40 - 4) / 2)
        dealt.playerHands[1].type shouldBe HandType.NORMAL
    }
}