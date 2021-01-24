package org.jd.benoggl.services

import org.jd.benoggl.models.Hand

data class DealtCards(val playerHands: List<Hand>, val dabb: Hand)
