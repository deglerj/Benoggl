package org.jd.benoggl.common.models

data class Player(val uid: String, val name: String, var hand: Hand = Hand.empty()) {
}
