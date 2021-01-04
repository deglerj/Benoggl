package org.jd.benoggl.rest

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.smallrye.common.constraint.Assert.assertNotNull
import org.jd.benoggl.model.BiddingState
import org.jd.benoggl.model.GameState
import org.jd.benoggl.model.GameType
import org.jd.benoggl.model.RoundState
import org.jd.benoggl.persistence.GameEntity
import org.jd.benoggl.rest.dtos.GameDto
import org.jd.benoggl.rest.dtos.PlayerDto
import org.jd.benoggl.truncateAllTables
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.transaction.Transactional
import javax.ws.rs.core.MediaType

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
@Transactional
class GameResourceTest {

    @Inject
    @field: Default
    internal lateinit var entityManager: EntityManager

    @BeforeEach
    fun prepare() {
        entityManager.truncateAllTables()
    }

    @Test
    fun createValidNewGame() {
        val player1 = PlayerDto("1", "player 1")
        val player2 = PlayerDto("2", "player 2")
        val gameDto = GameDto("3", GameState.RUNNING, GameType.NORMAL, listOf(player1, player2))

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(gameDto)
            .`when`().put("/game")
            .then()
            .statusCode(204)

        val gameEntity = GameEntity.findByUid("3")
        assertNotNull(gameEntity)
        assertEquals("3", gameEntity!!.uid)
        assertEquals(GameState.RUNNING, gameEntity.state)
        assertEquals(GameType.NORMAL, gameEntity.type)
        assertEquals("1", gameEntity.players[0].uid)
        assertEquals("2", gameEntity.players[1].uid)
        assertEquals(1, gameEntity.rounds.size)
        assertEquals(RoundState.BIDDING, gameEntity.rounds[0].state)
        assertEquals(BiddingState.RUNNING, gameEntity.rounds[0].bidding.state)
        assertNotNull(gameEntity.rounds[0].dabb)
        assertEquals(2, gameEntity.rounds[0].playerHands.size)
    }

    @Test
    fun createNewGameWithMissingValues() {

    }

    @Test
    fun createNewGameWithInvalidState() {

    }

    @Test
    fun createNewGameWithTooFewPlayers() {

    }

    @Test
    fun createNewGameWithTooManyPlayers() {

    }

    @Test
    fun createNewGameWithDuplicateUid() {

    }

    @Test
    fun createNewGameWithNonUniquePlayerIds() {

    }
}