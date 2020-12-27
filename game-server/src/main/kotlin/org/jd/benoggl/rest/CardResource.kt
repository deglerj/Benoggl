package org.jd.benoggl.rest

import org.jd.benoggl.mapper.toDto
import org.jd.benoggl.mapper.toEntity
import org.jd.benoggl.mapper.toModel
import org.jd.benoggl.persistence.CardEntity
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Valid
import javax.validation.Validator
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import kotlin.streams.asSequence

@Path("/card")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
class CardResource {

    @Inject
    @field: Default
    internal lateinit var validator: Validator

    @GET
    fun getAll() = CardEntity.streamAll().asSequence()
        .map { it.toModel().toDto() }
        .toList()

    @PUT
    fun add(@Valid card: CardDto) {
        val entity = card.toModel().toEntity()
        entity.persist()
    }

}