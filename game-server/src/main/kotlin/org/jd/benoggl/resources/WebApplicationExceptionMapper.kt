package org.jd.benoggl.resources

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class WebApplicationExceptionMapper : ExceptionMapper<WebApplicationException> {

    override fun toResponse(exception: WebApplicationException?): Response {
        return Response.fromResponse(exception!!.response).entity(exception.message).build()
    }
}