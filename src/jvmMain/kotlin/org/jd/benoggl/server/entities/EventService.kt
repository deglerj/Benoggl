package org.jd.benoggl.server.entities

import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.reactive.asFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jd.benoggl.common.events.Event
import org.jd.benoggl.common.models.Game
import org.jd.benoggl.server.repositories.EventRepository
import org.springframework.stereotype.Service

@Service
class EventService(
    val eventRepo: EventRepository
) {

    private val json = Json

    suspend fun applyAndSave(event: Event, gameUid: String): Game {
        val game = replayEventsForGame(gameUid)

        val eventWithChildEvents = withChildEvents(game, event)
        eventWithChildEvents.forEach { it.apply(game) }

        eventRepo.saveAll(eventWithChildEvents.map { createEntity(it, gameUid) }).blockLast()
        return game
    }

    private fun withChildEvents(game: Game, event: Event, events: MutableList<Event> = mutableListOf()): List<Event> {
        events.add(event)
        event.createChildEvents(game).forEach { withChildEvents(game, it, events) }
        return events
    }

    suspend fun replayEventsForGame(gameUid: String): Game {
        val game = Game(gameUid)

        eventRepo.findAllByGameUidOrderById(gameUid).asFlow()
            .map { deserialize(it.data) }
            .onEach { it.apply(game) }
            .lastOrNull()

        return game
    }


    internal fun createEntity(event: Event, gameUid: String): EventEntity =
        EventEntity(gameUid = gameUid, data = serialize(event))

    internal fun serialize(event: Event): String = json.encodeToString(event)


    internal fun deserialize(data: String): Event = json.decodeFromString(data)

}