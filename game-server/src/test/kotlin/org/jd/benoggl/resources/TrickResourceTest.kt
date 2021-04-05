package org.jd.benoggl.resources

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.jd.benoggl.entities.*
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.models.*
import org.jd.benoggl.resources.dtos.TrickDto
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
class TrickResourceTest {

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
                Player("player2", "Player 2"),
                Player("player3", "Player 3")
            )
        )
        roundService.startNewRound(GameEntity.findByUid("game1")!!.toModel())
        val round = RoundEntity.findByNumber(0, "game1")!!
        round.state = RoundState.TRICKING
        round.persist()

        val trick1 = TrickEntity()
        round.tricks.add(trick1)
        trick1.round = round
        trick1.number = 0
        trick1.state = TrickState.FINISHED
        trick1.winner = PlayerEntity.findByUid("player1", "game1")
        trick1.persist()

        val trick2 = TrickEntity()
        round.tricks.add(trick2)
        trick2.round = round
        trick2.number = 1
        trick2.state = TrickState.RUNNING
        val player2 = PlayerEntity.findByUid("player2", "game1")!!
        val player3 = PlayerEntity.findByUid("player3", "game1")!!
        trick2.pendingPlayers = mutableListOf(player2, player3)
        player2.pendingInTrick = trick2
        player3.pendingInTrick = trick2
        trick2.persist()
    }

    @Test
    fun getTricks() {
        val dtos = given()
            .`when`().get("/game/game1/round/0/trick")
            .then()
            .statusCode(200)
            .extract().body().`as`(Array<TrickDto>::class.java)

        dtos[0].number shouldBe 0
        dtos[0].state shouldBe TrickState.FINISHED
        dtos[0].winnerUid shouldBe "player1"
        dtos[0].pendingPlayerUids!!.shouldBeEmpty()
        dtos[1].number shouldBe 1
        dtos[1].state shouldBe TrickState.RUNNING
        dtos[1].winnerUid.shouldBeNull()
        dtos[1].pendingPlayerUids.shouldContainExactly("player2", "player3")
    }

    @Test
    fun getTrick_validNumber() {
        val dto = given()
            .`when`().get("/game/game1/round/0/trick/1")
            .then()
            .statusCode(200)
            .extract().body().`as`(TrickDto::class.java)

        dto.number shouldBe 1
        dto.state shouldBe TrickState.RUNNING
        dto.winnerUid.shouldBeNull()
        dto.pendingPlayerUids.shouldContainExactly("player2", "player3")
    }

    @Test
    fun getTrick_invalidNumber() {
        given()
            .`when`().get("/game/game1/round/0/trick/42")
            .then()
            .statusCode(404)
    }
}
