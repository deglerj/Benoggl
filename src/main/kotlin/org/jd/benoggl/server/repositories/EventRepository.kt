package org.jd.benoggl.server.repositories

import org.jd.benoggl.server.entities.EventEntity
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface EventRepository : R2dbcRepository<EventEntity, Long> {

    fun findAllByGameUidOrderById(gameUid: String): Flux<EventEntity>

}