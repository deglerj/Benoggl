package org.jd.benoggl.resources

import org.jd.benoggl.entities.PlayerEntity
import org.jd.benoggl.entities.RoundEntity
import org.jd.benoggl.mappers.toDto
import org.jd.benoggl.mappers.toModel
import org.jd.benoggl.models.Player
import org.jd.benoggl.resources.dtos.BidDto
import org.jd.benoggl.resources.dtos.BiddingDto
import org.jd.benoggl.resources.dtos.PlayerDto
import org.jd.benoggl.services.BidService
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Validator
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/game/{gameUid}/round/{roundNumber}/bidding")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
class BiddingResource {

    @Inject
    @field: Default
    internal lateinit var bidService: BidService

    @Inject
    @field: Default
    internal lateinit var validator: Validator

    @GET
    fun getBidding(@PathParam("gameUid") gameUid: String, @PathParam("roundNumber") roundNumber: Int): BiddingDto {
        val round = findRound(roundNumber, gameUid)
        return round.bidding.toModel().toDto()
    }

    @GET
    @Path("/bid")
    fun getBids(@PathParam("gameUid") gameUid: String, @PathParam("roundNumber") roundNumber: Int): List<BidDto> {
        val round = findRound(roundNumber, gameUid)
        return round.bidding.bids.map { it.toModel().toDto() }
    }

    @PUT
    @Path("/bid")
    fun addBid(
        @PathParam("gameUid") gameUid: String,
        @PathParam("roundNumber") roundNumber: Int,
        bid: BidDto
    ) {
        validator.validateRestDto(bid)

        findRound(roundNumber, gameUid)

        val playerUid = bid.playerUid!!
        val player = findPlayer(playerUid, gameUid)

        if (!bidService.isCurrentChallenger(roundNumber, gameUid, player)) {
            throw BadRequestException("Player $playerUid is not the current challenger in round $roundNumber of game $gameUid")
        }

        if (!bidService.isValidBid(roundNumber, gameUid, player, bid.points!!)) {
            throw BadRequestException("Bid is not valid. Please ensure it's higher than the other bids.")
        }

        bidService.placeBid(roundNumber, gameUid, player, bid.points)
    }

    private fun findPlayer(playerUid: String, gameUid: String): Player {
        return PlayerEntity.findByUid(playerUid, gameUid)?.toModel()
            ?: throw BadRequestException("Player $playerUid does not exist in game $gameUid")
    }

    @GET
    @Path("/challenger")
    fun getChallengers(
        @PathParam("gameUid") gameUid: String,
        @PathParam("roundNumber") roundNumber: Int
    ): List<PlayerDto> {
        val round = findRound(roundNumber, gameUid)
        return round.bidding.challengers.map { it.toModel().toDto() }
    }

    @DELETE
    @Path("/challenger/{playerUid}")
    fun removeChallenger(
        @PathParam("gameUid") gameUid: String,
        @PathParam("roundNumber") roundNumber: Int,
        @PathParam("playerUid") playerUid: String
    ) {
        findRound(roundNumber, gameUid)

        val player = findPlayer(playerUid, gameUid)

        if (!bidService.isCurrentChallenger(roundNumber, gameUid, player)) {
            throw BadRequestException("Player $playerUid is not the current challenger in round $roundNumber of game $gameUid")
        }

        bidService.removeChallenger(roundNumber, gameUid, player)
    }

    private fun findRound(roundNumber: Int, gameUid: String): RoundEntity {
        return RoundEntity.findByNumber(roundNumber, gameUid)
            ?: throw BadRequestException("Round $roundNumber for game $gameUid does not exist")
    }

}