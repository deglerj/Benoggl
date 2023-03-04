package org.jd.benoggl.common.models

import kotlinx.serialization.Serializable

@Serializable
data class Hand(val cards: MutableList<Card>) {

    companion object {
        fun empty() = Hand(mutableListOf())
    }

}
