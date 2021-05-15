package org.jd.benoggl

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test

@QuarkusTest
internal class CollectionExtensionsTest {

    @Test
    fun removeFirst_removeOnlyOne() {
        val sut = mutableListOf(1, 2, 2, 3)

        val removed = sut.removeFirst { it == 2 }

        removed shouldBe 2
        sut shouldContainExactly listOf(1, 2, 3)
    }

    @Test
    fun removeFirst_ignoresUnmatched() {
        val sut = mutableListOf(1, 2, 3)

        val removed = sut.removeFirst { it == 4 }

        removed shouldBe null
        sut shouldContainExactly listOf(1, 2, 3)
    }
}