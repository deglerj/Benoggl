package org.jd.benoggl.resources

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.smallrye.common.constraint.Assert.assertNotNull
import org.jd.benoggl.entities.GameEntity
import org.jd.benoggl.model.BiddingState
import org.jd.benoggl.model.GameState
import org.jd.benoggl.model.GameType
import org.jd.benoggl.model.RoundState
import org.jd.benoggl.resources.dtos.GameDto
import org.jd.benoggl.resources.dtos.PlayerDto
import org.jd.benoggl.truncateAllTables
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.transaction.Transactional
import javax.ws.rs.core.MediaType

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
@Transactional
class GameResourceTest {

    companion object {
        val player1 = PlayerDto("1", "player 1")
        val player2 = PlayerDto("2", "player 2")

        @JvmStatic
        @Suppress("unused")
        fun createGamesWithMissingValues(): Stream<GameDto> = Stream.of(
            GameDto(null, GameState.RUNNING, GameType.NORMAL, listOf(player1, player2)),
            GameDto("uid", null, GameType.NORMAL, listOf(player1, player2)),
            GameDto("uid", GameState.RUNNING, null, listOf(player1, player2)),
            GameDto("uid", GameState.RUNNING, GameType.NORMAL, null)
        )
    }

    @Inject
    @field: Default
    internal lateinit var entityManager: EntityManager

    @BeforeEach
    fun prepare() {
        entityManager.truncateAllTables()
    }

    @Test
    fun createValidNewGame() {
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

    @ParameterizedTest
    @MethodSource("createGamesWithMissingValues")
    fun createNewGameWithMissingValues(gameDto: GameDto) {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(gameDto)
            .`when`().put("/game")
            .then()
            .statusCode(400)
    }

    @Test
    fun createNewGameWithInvalidState() {
        val gameDto = GameDto("uid", GameState.FINISHED, GameType.NORMAL, listOf(player1, player2))

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(gameDto)
            .`when`().put("/game")
            .then()
            .statusCode(400)
    }

    @Test
    fun createNewGameWithTooFewPlayers() {
        val gameDto = GameDto(null, GameState.RUNNING, GameType.NORMAL, listOf(player1))

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(gameDto)
            .`when`().put("/game")
            .then()
            .statusCode(400)
    }

    @Test
    fun createNewGameWithTooManyPlayers() {
        val player3 = PlayerDto("3", "player 3")
        val player4 = PlayerDto("4", "player 4")
        val player5 = PlayerDto("5", "player 5")

        val gameDto =
            GameDto(null, GameState.RUNNING, GameType.NORMAL, listOf(player1, player2, player3, player4, player5))

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(gameDto)
            .`when`().put("/game")
            .then()
            .statusCode(400)
    }

    @Test
    fun createNewGameWithDuplicateUid() {
        val gameDto = GameDto("uid", GameState.RUNNING, GameType.NORMAL, listOf(player1, player2))

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(gameDto)
            .`when`().put("/game")
            .then()
            .statusCode(204)

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(gameDto)
            .`when`().put("/game")
            .then()
            .statusCode(400)
    }

    @Test
    fun createNewGameWithNonUniquePlayerIds() {
        val gameDto = GameDto("uid", GameState.RUNNING, GameType.NORMAL, listOf(player1, player1))

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(gameDto)
            .`when`().put("/game")
            .then()
            .statusCode(400)
    }

    @Test
    fun getGame() {
        val putDto = GameDto("uid", GameState.RUNNING, GameType.NORMAL, listOf(player1, player2))
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(putDto)
            .`when`().put("/game")
            .then()
            .statusCode(204)

        val getDto = given()
            .contentType(MediaType.APPLICATION_JSON)
            .`when`().get("/game/uid")
            .then()
            .statusCode(200)
            .extract().body().`as`(GameDto::class.java)

        assertEquals(putDto, getDto)
    }

    @Test
    fun getGameWithUnknownUid() {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .`when`().get("/game/whatever")
            .then()
            .statusCode(404)
    }
}