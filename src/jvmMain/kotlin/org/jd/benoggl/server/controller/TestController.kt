package org.jd.benoggl.server.controller

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jd.benoggl.common.events.CardsDealtEvent
import org.jd.benoggl.common.events.Event
import org.jd.benoggl.common.events.PlayerAddedEvent
import org.jd.benoggl.common.events.RoundStartedEvent
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux

@Controller
class TestController {

    private val json = Json

    @MessageMapping("request-event-stream")
    fun requestEventStream(): Flux<String> {
        return listOf(
            PlayerAddedEvent("player1"),
            PlayerAddedEvent("player2"),
            PlayerAddedEvent("player3"),
            RoundStartedEvent,
            CardsDealtEvent()
        )
            .map { serialize(it) }
            .toFlux()
    }

    internal fun serialize(event: Event): String = json.encodeToString(event)
}