package org.jd.benoggl.resources

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.jd.benoggl.entities.GameEntity
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.models.GameType
import org.jd.benoggl.models.Player
import org.jd.benoggl.resources.dtos.RoundDto
import org.jd.benoggl.services.GameService
import org.jd.benoggl.services.RoundService
import org.jd.benoggl.truncateAllTables
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.transaction.Transactional

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
@Transactional
class RoundResourceTest {

    @Inject
    @field: Default
    internal lateinit var entityManager: EntityManager

    @Inject
    @field: Default
    internal lateinit var gameService: GameService

    @Inject
    @field:Default
    internal lateinit var roundService: RoundService

    @BeforeEach
    fun prepare() {
        entityManager.truncateAllTables()

        gameService.createAndStartGame(
            "game1", GameType.NORMAL,
            listOf(
                Player("player1", "Player 1"),
                Player("player2", "Player 2")
            )
        )
        roundService.startNewRound(GameEntity.findByUid("game1")!!.toModel())
    }

    @Test
    fun getRounds() {
        val dtos = given()
            .`when`().get("/game/game1/round")
            .then()
            .statusCode(200)
            .extract().body().`as`(Array<RoundDto>::class.java)

        dtos
            .map(RoundDto::number)
            .shouldContainExactly(0, 1)
    }

    @Test
    fun getRoundsForUnknownGame() {
        given()
            .`when`().get("/game/whatever/round")
            .then()
            .statusCode(404)
    }

    @Test
    fun getRound() {
        val dto = given()
            .`when`().get("/game/game1/round/0")
            .then()
            .statusCode(200)
            .extract().body().`as`(RoundDto::class.java)

        dto.number shouldBe 0
    }

    @Test
    fun getRoundForUnknownGame() {
        given()
            .`when`().get("/game/whatever/round/0")
            .then()
            .statusCode(404)
    }

    @Test
    fun getRoundWithUnknownNumber() {
        given()
            .`when`().get("/game/game1/round/42")
            .then()
            .statusCode(404)
    }
}