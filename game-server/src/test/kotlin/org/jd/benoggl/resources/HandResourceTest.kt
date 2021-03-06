package org.jd.benoggl.resources

import io.kotest.matchers.shouldBe
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.jd.benoggl.entities.*
import org.jd.benoggl.models.*
import org.jd.benoggl.resources.dtos.HandDto
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
class HandResourceTest {

    @Inject
    @field: Default
    internal lateinit var entityManager: EntityManager

    @BeforeEach
    fun prepare() {
        entityManager.truncateAllTables()

        val game = createGame("1")
        val player = createPlayer("1", game)
        createHand(player, game, 1)
        createHand(player, game, 2)
        createHand(player, game, 2)
    }

    @Test
    fun findsValidHand() {
        val dto = given()
            .header("Player-UID", "1")
            .`when`().get("/game/1/round/1/hand")
            .then()
            .statusCode(200)
            .extract().body().`as`(HandDto::class.java)

        dto.type shouldBe HandType.NORMAL
    }

    @Test
    fun failsOnMissingHand() {
        given()
            .header("Player-UID", "1")
            .`when`().get("/game/1/round/42/hand")
            .then()
            .statusCode(400)
    }

    @Test
    fun failsOnDuplicateHand() {
        given()
            .header("Player-UID", "1")
            .`when`().get("/game/1/round/2/hand")
            .then()
            .statusCode(500)
    }

    private fun createGame(gameUid: String): GameEntity {
        val game = GameEntity()
        game.uid = gameUid
        game.type = GameType.NORMAL
        game.state = GameState.RUNNING
        game.persist()
        return game
    }

    private fun createPlayer(playerUid: String, game: GameEntity): PlayerEntity {
        val player = PlayerEntity()
        player.uid = playerUid
        player.name = "Player $playerUid"
        player.game = game
        player.persist()
        return player
    }

    private fun createHand(player: PlayerEntity, game: GameEntity, roundNumber: Int) {
        val bidding = BiddingEntity()
        bidding.state = BiddingState.RUNNING
        bidding.persist()

        val dabb = HandEntity()
        dabb.type = HandType.DABB
        dabb.persist()

        val discard = HandEntity()
        discard.type = HandType.DISCARD
        discard.persist()

        val round = RoundEntity()
        round.number = roundNumber
        round.state = RoundState.TRICKING
        round.type = RoundType.NORMAL
        round.game = game
        round.bidding = bidding
        round.dabb = dabb
        round.discard = discard
        round.persist()

        val hand = HandEntity()
        hand.type = HandType.NORMAL
        hand.cards = mutableListOf()
        hand.persist()

        val playerHand = PlayerHandEntity()
        playerHand.player = PlayerEntity.findById(player.id!!)!!
        playerHand.round = round
        playerHand.hand = hand
        playerHand.persist()
    }
}