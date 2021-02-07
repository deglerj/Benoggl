package org.jd.benoggl.resources

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.response.Response
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.jd.benoggl.entities.*
import org.jd.benoggl.mappers.toDto
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.models.*
import org.jd.benoggl.resources.dtos.MeldDto
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
import org.hamcrest.Matchers.`is` as Is

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
@Transactional
class MeldResourceTest {

    @Inject
    @field: Default
    internal lateinit var entityManager: EntityManager

    @Inject
    @field: Default
    internal lateinit var gameService: GameService

    @Inject
    @field:Default
    internal lateinit var roundService: RoundService

    private val binokelAndLeavesPair = listOf(
        Card(Suit.LEAVES, Rank.OBER),
        Card(Suit.BELLS, Rank.UNTER),
        Card(Suit.LEAVES, Rank.UNTER)
    )

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
        round.state = RoundState.MELDING
        round.trump = Suit.BELLS
        round.persist()

        giveCardsToPlayer("player1", binokelAndLeavesPair)
        giveCardsToPlayer("player2", binokelAndLeavesPair)
        giveCardsToPlayer("player3", binokelAndLeavesPair)
    }

    private fun giveCardsToPlayer(playerUid: String, cards: List<Card>) {
        val hand = HandEntity.findForPlayer(playerUid, 0, "game1")
        hand.cards = cards
            .map {
                val cardEntity = CardEntity()
                cardEntity.suit = it.suit
                cardEntity.rank = it.rank
                cardEntity.hand = hand
                return@map cardEntity
            }
            .toMutableList()
        hand.persist()
    }

    @Test
    fun getMeldsBeforeAnyMeldsPut() {
        val dtos = given()
            .`when`().get("/game/game1/round/0/meld")
            .then()
            .statusCode(200)
            .extract().body().`as`(Array<MeldDto>::class.java)

        assertThat(dtos, Is(emptyArray()))
    }

    @Test
    fun addValidMelds() {
        addMeld(40, "player1", binokelAndLeavesPair)
            .then()
            .statusCode(204)
        addMeld(40, "player2", binokelAndLeavesPair)
            .then()
            .statusCode(204)
        addMeld(40, "player3", binokelAndLeavesPair)
            .then()
            .statusCode(204)

        val dtos = given()
            .`when`().get("/game/game1/round/0/meld")
            .then()
            .statusCode(200)
            .extract().body().`as`(Array<MeldDto>::class.java)

        assertThat(dtos, Is(arrayWithSize(3)))
    }

    @Test
    fun addInvalidSecondMeldForPlayer() {
        addMeld(40, "player1", binokelAndLeavesPair)
            .then()
            .statusCode(204)

        addMeld(40, "player1", binokelAndLeavesPair)
            .then()
            .statusCode(400)
    }

    @Test
    fun addInvalidTooHighMeld() {
        addMeld(500, "player1", binokelAndLeavesPair)
            .then()
            .statusCode(400)
    }

    @Test
    fun addValidTooLowMeld() {
        addMeld(20, "player1", binokelAndLeavesPair)
            .then()
            .statusCode(204)
    }

    @Test
    fun addInvalidMeldWithUnavailableCards() {
        addMeld(
            40,
            "player1",
            listOf(
                Card(Suit.LEAVES, Rank.OBER),
                Card(Suit.LEAVES, Rank.UNTER),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.HEARTS, Rank.UNTER)
            )
        )
            .then()
            .statusCode(400)
    }

    private fun addMeld(points: Int, playerUid: String, cards: Collection<Card>): Response {
        val meldDto = MeldDto(points = points, playerUid = playerUid, cards = cards.map(Card::toDto))
        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(meldDto)
            .`when`().put("/game/game1/round/0/meld")
    }
}
