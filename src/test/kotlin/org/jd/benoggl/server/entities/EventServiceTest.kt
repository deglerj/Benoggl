package org.jd.benoggl.server.entities

import io.kotest.common.runBlocking
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.jd.benoggl.common.events.PlayerAddedEvent
import org.jd.benoggl.common.events.RoundStartedEvent
import org.jd.benoggl.common.models.RoundState
import org.jd.benoggl.server.repositories.EventRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

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

            eventRepo.count().block() shouldBe 1
            game.players shouldHaveSize 1
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
            eventRepo.count().block() shouldBe 8
            game.players.forEach { it.hand.cards shouldHaveAtLeastSize 1 }
            game.dabb.cards shouldHaveSize 4
            game.currentRound.state shouldBe RoundState.BIDDING
        }
    }

    @Test
    fun replayEventsForGame_forValidGameUid_replaysEvents() {
        runBlocking {
            val event = PlayerAddedEvent("player1")
            val gameUid = UUID.randomUUID().toString()
            sut.applyAndSave(event, gameUid)

            val game = sut.replayEventsForGame(gameUid)

            game.players shouldHaveSize 1
        }
    }
}