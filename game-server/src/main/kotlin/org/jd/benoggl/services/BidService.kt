package org.jd.benoggl.services

import org.jd.benoggl.model.Player
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional

@ApplicationScoped
@Transactional
class BidService {

    fun isCurrentChallenger(gameUid: String, roundNumber: Int, player: Player) =
        findCurrentChallengerUid(gameUid, roundNumber) == player.uid

    fun removeChallenger(gameUid: String, roundNumber: Int, player: Player) {
        // TODO implement
    }

    fun isValidBid(gameUid: String, roundNumber: Int, player: Player, points: Int): Boolean {
        if (!isCurrentChallenger(gameUid, roundNumber, player)) {
            return false
        }

        // TODO implement
        return true
    }

    fun placeBid(gameUid: String, roundNumber: Int, player: Player, points: Int) {
        // TODO implement
    }

    private fun findCurrentChallengerUid(gameUid: String, roundNumber: Int): String? {
        // TODO implement
        return null
    }
}