package org.jd.benoggl.resources

import javax.validation.Validator
import javax.ws.rs.BadRequestException

fun Validator.validateRestDto(dto: Any?) {
    if (dto == null) {
        return
    }

    val validationResult = this.validate(dto)
    if (validationResult.isNotEmpty()) {
        var messages = validationResult.joinToString("\n") { "${it.propertyPath} ${it.message}" }
        throw BadRequestException(messages)
    }
}