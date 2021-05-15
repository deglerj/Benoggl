package org.jd.benoggl.resources

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.response.Response
import org.jd.benoggl.entities.*
import org.jd.benoggl.mappers.toDto
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.models.*
import org.jd.benoggl.resources.dtos.DiscardDto
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
internal class DiscardResourceTest {

    @Inject
    @field: Default
    internal lateinit var entityManager: EntityManager

    @Inject
    @field: Default
    internal lateinit var gameService: GameService

    @Inject
    @field:Default
    internal lateinit var roundService: RoundService

    private val setOfHearts = listOf(
        Card(Suit.HEARTS, Rank.UNTER),
        Card(Suit.HEARTS, Rank.OBER),
        Card(Suit.HEARTS, Rank.KING),
        Card(Suit.HEARTS, Rank.TEN),
        Card(Suit.HEARTS, Rank.ACE)
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
        round.bidding.highestBidder = PlayerEntity.findByUid("player1", "game1")
        round.state = RoundState.DISCARDING
        round.persist()

        giveCardsToPlayer("player1", setOfHearts)
        giveCardsToPlayer("player2", setOfHearts)
        giveCardsToPlayer("player3", setOfHearts)
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
    fun discardValidCardsForValidPlayer() {
        discard(
            "player1", listOf(
                Card(Suit.HEARTS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.HEARTS, Rank.TEN)
            )
        )
            .then()
            .statusCode(204)
    }

    @Test
    fun discardCardsNotOnHand() {
        discard(
            "player1", listOf(
                Card(Suit.BELLS, Rank.UNTER),
                Card(Suit.BELLS, Rank.OBER),
                Card(Suit.BELLS, Rank.KING),
                Card(Suit.BELLS, Rank.TEN)
            )
        )
            .then()
            .statusCode(400)
    }

    @Test
    fun discardTooFewCards() {
        discard(
            "player1", listOf(
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.HEARTS, Rank.TEN)
            )
        )
            .then()
            .statusCode(400)
    }


    @Test
    fun discardTooManyCards() {
        discard(
            "player1", listOf(
                Card(Suit.HEARTS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.HEARTS, Rank.TEN),
                Card(Suit.HEARTS, Rank.ACE)
            )
        )
            .then()
            .statusCode(400)
    }

    @Test
    fun discardCardsByInvalidPlayer() {
        discard(
            "player2", listOf(
                Card(Suit.HEARTS, Rank.UNTER),
                Card(Suit.HEARTS, Rank.OBER),
                Card(Suit.HEARTS, Rank.KING),
                Card(Suit.HEARTS, Rank.TEN)
            )
        )
            .then()
            .statusCode(400)
    }

    private fun discard(playerUid: String, cards: List<Card>): Response {
        val discardDto = DiscardDto(cards.map(Card::toDto), playerUid)
        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(discardDto)
            .`when`().post("/game/game1/round/0/discard")
    }

}