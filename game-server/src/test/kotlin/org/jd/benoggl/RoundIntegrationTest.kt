package org.jd.benoggl

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import org.jd.benoggl.entities.CardEntity
import org.jd.benoggl.entities.GameEntity
import org.jd.benoggl.entities.HandEntity
import org.jd.benoggl.entities.RoundEntity
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.models.*
import org.jd.benoggl.resources.BiddingResource
import org.jd.benoggl.resources.HandResource
import org.jd.benoggl.resources.MeldResource
import org.jd.benoggl.resources.RoundResource
import org.jd.benoggl.resources.dtos.BidDto
import org.jd.benoggl.resources.dtos.CardDto
import org.jd.benoggl.resources.dtos.MeldDto
import org.jd.benoggl.services.GameService
import org.jd.benoggl.services.RoundService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.transaction.Transactional

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
@Transactional
class RoundIntegrationTest {

    @Inject
    @field: Default
    internal lateinit var entityManager: EntityManager

    @Inject
    @field: Default
    internal lateinit var gameService: GameService

    @Inject
    @field:Default
    internal lateinit var roundService: RoundService

    @Inject
    @field:Default
    internal lateinit var biddingResource: BiddingResource

    @Inject
    @field:Default
    internal lateinit var roundResource: RoundResource

    @Inject
    @field:Default
    internal lateinit var meldResource: MeldResource

    @Inject
    @field:Default
    internal lateinit var handResource: HandResource


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

        val player1Hand = HandEntity.findForPlayer("player1", 0, "game1")
        val player2Hand = HandEntity.findForPlayer("player2", 0, "game1")
        val player3Hand = HandEntity.findForPlayer("player3", 0, "game1")
        val dabb = RoundEntity.findByNumber(0, "game1")!!.dabb

        redealCards(
            player1Hand,
            Card(Suit.HEARTS, Rank.OBER),
            Card(Suit.HEARTS, Rank.KING),
            Card(Suit.HEARTS, Rank.TEN),
            Card(Suit.BELLS, Rank.TEN),
            Card(Suit.LEAVES, Rank.UNTER),
            Card(Suit.LEAVES, Rank.KING),
            Card(Suit.LEAVES, Rank.TEN),
            Card(Suit.LEAVES, Rank.ACE),
            Card(Suit.ACORNS, Rank.UNTER),
            Card(Suit.ACORNS, Rank.OBER),
            Card(Suit.ACORNS, Rank.KING),
            Card(Suit.ACORNS, Rank.TEN)
        )

        redealCards(
            player2Hand,
            Card(Suit.HEARTS, Rank.UNTER),
            Card(Suit.HEARTS, Rank.TEN),
            Card(Suit.BELLS, Rank.UNTER),
            Card(Suit.BELLS, Rank.KING),
            Card(Suit.BELLS, Rank.ACE),
            Card(Suit.LEAVES, Rank.UNTER),
            Card(Suit.LEAVES, Rank.OBER),
            Card(Suit.LEAVES, Rank.OBER),
            Card(Suit.LEAVES, Rank.TEN),
            Card(Suit.ACORNS, Rank.OBER),
            Card(Suit.ACORNS, Rank.KING),
            Card(Suit.ACORNS, Rank.ACE)
        )

        redealCards(
            player3Hand,
            Card(Suit.HEARTS, Rank.KING),
            Card(Suit.HEARTS, Rank.ACE),
            Card(Suit.HEARTS, Rank.ACE),
            Card(Suit.BELLS, Rank.OBER),
            Card(Suit.BELLS, Rank.OBER),
            Card(Suit.BELLS, Rank.TEN),
            Card(Suit.BELLS, Rank.ACE),
            Card(Suit.LEAVES, Rank.KING),
            Card(Suit.LEAVES, Rank.ACE),
            Card(Suit.ACORNS, Rank.UNTER),
            Card(Suit.ACORNS, Rank.TEN),
            Card(Suit.ACORNS, Rank.ACE)
        )

        redealCards(
            dabb,
            Card(Suit.HEARTS, Rank.UNTER),
            Card(Suit.HEARTS, Rank.OBER),
            Card(Suit.BELLS, Rank.UNTER),
            Card(Suit.BELLS, Rank.KING)
        )
    }

    private fun redealCards(hand: HandEntity, vararg cards: Card) {
        clearDealtCards(hand)

        // Create and assign new cards
        cards
            .map { createCardEntity(it.suit, it.rank) }
            .forEach { card ->
                card.hand = hand
                card.persist()
                hand.cards.add(card)
            }

        hand.persist()
    }

    private fun createCardEntity(suit: Suit, rank: Rank): CardEntity {
        val entity = CardEntity()
        entity.suit = suit
        entity.rank = rank
        return entity
    }

    private fun clearDealtCards(hand: HandEntity) {
        hand.cards.forEach { it.hand = null }
        hand.cards.forEach { it.delete() }
        hand.cards.clear()
    }

    @Test
    fun player3Wins() {
        roundResource.getRound("game1", 0).state shouldBe RoundState.BIDDING

        // Bidding: Player 3 gets round for 250
        biddingResource.addBid("game1", 0, BidDto(150, "player1"))
        biddingResource.addBid("game1", 0, BidDto(200, "player2"))
        biddingResource.removeChallenger("game1", 0, "player1")
        biddingResource.addBid("game1", 0, BidDto(250, "player3"))
        biddingResource.removeChallenger("game1", 0, "player2")

        biddingResource.getBids("game1", 0) shouldHaveSize 3
        biddingResource.getBidding("game1", 0).highestBid shouldBe 250
        biddingResource.getBidding("game1", 0).highestBidderUid shouldBe "player3"
        roundResource.getRound("game1", 0).state shouldBe RoundState.MELDING
        handResource.getPlayerHand("player1", "game1", 0).cards!! shouldHaveSize 12
        handResource.getPlayerHand("player2", "game1", 0).cards!! shouldHaveSize 12
        handResource.getPlayerHand("player3", "game1", 0).cards!! shouldHaveSize 16

        // Melding: Player 1: 40, Player 2: 60, Player 3: 270, Trump: Bells
        meldResource.addMeld(
            "game1", 0, MeldDto(
                cards = listOf(
                    CardDto(Suit.HEARTS, Rank.OBER),
                    CardDto(Suit.HEARTS, Rank.KING), // Pair: 20
                    CardDto(Suit.BELLS, Rank.UNTER),
                    CardDto(Suit.BELLS, Rank.OBER),
                    CardDto(Suit.BELLS, Rank.KING),
                    CardDto(Suit.BELLS, Rank.TEN),
                    CardDto(Suit.BELLS, Rank.ACE), // Trump family: 150
                    CardDto(Suit.HEARTS, Rank.ACE),
                    CardDto(Suit.LEAVES, Rank.ACE),
                    CardDto(Suit.ACORNS, Rank.ACE) // Four aces: 100
                ),
                playerUid = "player3",
                points = 270,
                trump = Suit.BELLS
            )
        )
        meldResource.addMeld(
            "game1", 0, MeldDto(
                cards = listOf(
                    CardDto(Suit.HEARTS, Rank.OBER),
                    CardDto(Suit.HEARTS, Rank.KING), // Pair: 20
                    CardDto(Suit.ACORNS, Rank.OBER),
                    CardDto(Suit.ACORNS, Rank.KING) // Pair: 20
                ),
                playerUid = "player1",
                points = 40
            )
        )
        meldResource.addMeld(
            "game1", 0, MeldDto(
                cards = listOf(
                    CardDto(Suit.ACORNS, Rank.OBER),
                    CardDto(Suit.ACORNS, Rank.KING), // Pair: 20
                    CardDto(Suit.LEAVES, Rank.OBER),
                    CardDto(Suit.BELLS, Rank.UNTER) // Binokel: 40
                ),
                playerUid = "player2",
                points = 60
            )
        )

        meldResource.getMelds("game1", 0) shouldHaveSize 3
        roundResource.getRound("game1", 0).state shouldBe RoundState.TRICKING
    }


}