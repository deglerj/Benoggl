package org.jd.benoggl.common

import kotlin.test.fail


fun <T> assertContainsInAnyOrder(expected: Collection<T>, actual: Collection<T>?) {

    if (expected === actual) {
        return
    }

    if (actual == null) {
        fail("Expected a collection, but got null")
    }

    if (expected.size != actual.size) {
        fail("Lengths differ. Expected length is ${expected.size}, actual length is ${actual.size}.")
    }

    val missingInActual = expected.toMutableList() - actual
    if (missingInActual.isNotEmpty()) {
        fail("Actual collection has missing values: ${missingInActual.joinToString(", ")}")
    }

    val superfluousInActual = actual.toMutableList() - expected.toSet()
    if (superfluousInActual.isNotEmpty()) {
        fail("Actual collection has superfluous values: ${superfluousInActual.joinToString(", ")}")
    }

}