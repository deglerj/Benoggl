package org.jd.benoggl.resources

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.jd.benoggl.entities.GameEntity
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.models.BiddingState
import org.jd.benoggl.models.GameType
import org.jd.benoggl.models.Player
import org.jd.benoggl.resources.dtos.BidDto
import org.jd.benoggl.resources.dtos.BiddingDto
import org.jd.benoggl.resources.dtos.PlayerDto
import org.jd.benoggl.services.GameService
import org.jd.benoggl.services.RoundService
import org.jd.benoggl.truncateAllTables
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
class BiddingResourceTest {

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
    }

    @Test
    fun initialState() {
        val dto = given()
            .`when`().get("/game/game1/round/0/bidding")
            .then()
            .statusCode(200)
            .extract().body().`as`(BiddingDto::class.java)

        dto.state shouldBe BiddingState.RUNNING
        dto.highestBid shouldBe 0
        dto.highestBidderUid.shouldBeNull()
    }

    @Test
    fun placeValidInitialBid() {
        placeBid(150, "player1")

        val biddingDto = given()
            .`when`().get("/game/game1/round/0/bidding")
            .then()
            .statusCode(200)
            .extract().body().`as`(BiddingDto::class.java)
        val challengerDtos = given()
            .`when`().get("/game/game1/round/0/bidding/challenger")
            .then()
            .statusCode(200)
            .extract().body().`as`(Array<PlayerDto>::class.java)

        biddingDto.state shouldBe BiddingState.RUNNING
        biddingDto.highestBid shouldBe 150
        biddingDto.highestBidderUid shouldBe "player1"
        challengerDtos
            .map(PlayerDto::uid)
            .shouldContainExactly("player2", "player3")
    }

    @Test
    fun placeInitialBidWithInvalidPlayer() {
        val bidDto = BidDto(150, "player2")
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(bidDto)
            .`when`().put("/game/game1/round/0/bidding/bid")
            .then()
            .statusCode(400)
    }

    @Test
    fun placeInitialBidWithInvalidPoints() {
        val bidDto = BidDto(0, "player1")
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(bidDto)
            .`when`().put("/game/game1/round/0/bidding/bid")
            .then()
            .statusCode(400)
    }

    @Test
    fun placeHigherBid() {
        placeBid(150, "player1")
        placeBid(160, "player2")

        val biddingDto = given()
            .`when`().get("/game/game1/round/0/bidding")
            .then()
            .statusCode(200)
            .extract().body().`as`(BiddingDto::class.java)
        val challengerDtos = given()
            .`when`().get("/game/game1/round/0/bidding/challenger")
            .then()
            .statusCode(200)
            .extract().body().`as`(Array<PlayerDto>::class.java)


        biddingDto.state shouldBe BiddingState.RUNNING
        biddingDto.highestBid shouldBe 160
        biddingDto.highestBidderUid shouldBe "player2"
        challengerDtos
            .map(PlayerDto::uid)
            .shouldContainExactly("player1", "player3")
    }

    @Test
    fun removeChallenger_currentChallenger() {
        placeBid(150, "player1")

        given()
            .`when`()
            .delete("/game/game1/round/0/bidding/challenger/player2")
            .then()
            .statusCode(204)
    }

    @Test
    fun removeChallenger_notCurrentChallenger() {
        placeBid(150, "player1")

        given()
            .`when`()
            .delete("/game/game1/round/0/bidding/challenger/player1")
            .then()
            .statusCode(400)
    }

    @Test
    fun removeChallenger_lastChallenger() {
        placeBid(150, "player1")

        given()
            .`when`()
            .delete("/game/game1/round/0/bidding/challenger/player2")
            .then()
            .statusCode(204)
        given()
            .`when`()
            .delete("/game/game1/round/0/bidding/challenger/player3")
            .then()
            .statusCode(204)

        val biddingDto = given()
            .`when`().get("/game/game1/round/0/bidding")
            .then()
            .statusCode(200)
            .extract().body().`as`(BiddingDto::class.java)

        biddingDto.state shouldBe BiddingState.FINISHED
        biddingDto.highestBid shouldBe 150
        biddingDto.highestBidderUid shouldBe "player1"
    }

    @Test
    fun getBids_noBidsYet() {
        val bidDtos = given()
            .`when`().get("/game/game1/round/0/bidding/bid")
            .then()
            .statusCode(200)
            .extract().body().`as`(Array<BidDto>::class.java)

        bidDtos shouldHaveSize 0
    }

    @Test
    fun getBids() {
        placeBid(10, "player1")
        placeBid(20, "player2")
        placeBid(30, "player1")
        placeBid(40, "player2")

        val bidDtos = given()
            .`when`().get("/game/game1/round/0/bidding/bid")
            .then()
            .statusCode(200)
            .extract().body().`as`(Array<BidDto>::class.java)

        bidDtos
            .shouldContainExactly(
                BidDto(10, "player1"),
                BidDto(20, "player2"),
                BidDto(30, "player1"),
                BidDto(40, "player2")
            )
    }

    private fun placeBid(points: Int, playerUid: String) {
        val bidDto = BidDto(points, playerUid)
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(bidDto)
            .`when`().put("/game/game1/round/0/bidding/bid")
            .then()
            .statusCode(204)
    }
}
