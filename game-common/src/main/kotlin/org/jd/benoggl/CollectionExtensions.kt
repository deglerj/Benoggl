package org.jd.benoggl

inline fun <T> MutableList<T>.removeFirst(predicate: (T) -> Boolean): T? {
    val index = this.indexOfFirst(predicate)
    if (index == -1) {
        return null
    }

    return this.removeAt(index)
}