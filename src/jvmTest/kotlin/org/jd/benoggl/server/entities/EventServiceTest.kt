package org.jd.benoggl.server.entities

import kotlinx.coroutines.runBlocking
import org.jd.benoggl.common.events.PlayerAddedEvent
import org.jd.benoggl.common.events.RoundStartedEvent
import org.jd.benoggl.common.models.RoundState
import org.jd.benoggl.server.repositories.EventRepository
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
class EventServiceTest() {

    @Autowired
    lateinit var eventRepo: EventRepository

    @Autowired
    lateinit var sut: EventService

    @AfterEach
    fun cleanUp() {
        eventRepo.deleteAll().block()
    }

    @Test
    fun applyAndSave_forValidEvent_appliesAndSavesEvent() {
        runBlocking {
            val event = PlayerAddedEvent("player1")
            val game = sut.applyAndSave(event, UUID.randomUUID().toString())

            assertEquals(eventRepo.count().block(), 1)
            assertEquals(game.players.size, 1)
        }
    }

    @Test
    fun applyAndSave_forEventWithChildEvents_appliesAndSavesChildEvents() {
        runBlocking {
            val gameUid = UUID.randomUUID().toString()
            sut.applyAndSave(PlayerAddedEvent("player1"), gameUid)
            sut.applyAndSave(PlayerAddedEvent("player2"), gameUid)

            val game = sut.applyAndSave(RoundStartedEvent, gameUid)

            // 8 = 2 players + started + cards dealt + 2 hands dealt + dabb placed + bidding started
            assertEquals(eventRepo.count().block(), 8)
            game.players.forEach { assertTrue(it.hand.cards.size >= 1) }
            assertEquals(game.dabb.cards.size, 4)
            assertEquals(game.currentRound.state, RoundState.BIDDING)
        }
    }

    @Test
    fun replayEventsForGame_forValidGameUid_replaysEvents() {
        runBlocking {
            val event = PlayerAddedEvent("player1")
            val gameUid = UUID.randomUUID().toString()
            sut.applyAndSave(event, gameUid)

            val game = sut.replayEventsForGame(gameUid)

            assertTrue(game.players.size >= 1)
        }
    }
}