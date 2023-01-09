package org.jd.benoggl.common

inline fun <T> MutableCollection<T>.removeFirst(predicate: (T) -> Boolean): T? {
    val iterator = this.iterator()
    while (iterator.hasNext()) {
        val next = iterator.next()
        if (predicate(next)) {
            iterator.remove()
            return next
        }
    }

    return null
}

fun <T> MutableList<T>.copyOf(): MutableList<T> = mutableListOf<T>().also { it.addAll(this) }