package org.jd.benoggl.common.models

import kotlinx.serialization.Serializable

@Serializable
data class Dabb(val cards: Collection<Card>)
