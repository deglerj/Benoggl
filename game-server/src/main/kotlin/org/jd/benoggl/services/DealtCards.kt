package org.jd.benoggl.services

import org.jd.benoggl.model.Hand

data class DealtCards(val playerHands: List<Hand>, val dabb: Hand)
