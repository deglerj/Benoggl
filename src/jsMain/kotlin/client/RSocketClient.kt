package client

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.utils.io.core.*
import io.rsocket.kotlin.ExperimentalMetadataApi
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.core.RSocketConnector
import io.rsocket.kotlin.core.WellKnownMimeType
import io.rsocket.kotlin.ktor.client.RSocketSupport
import io.rsocket.kotlin.ktor.client.rSocket
import io.rsocket.kotlin.metadata.CompositeMetadata
import io.rsocket.kotlin.metadata.RoutingMetadata
import io.rsocket.kotlin.metadata.metadata
import io.rsocket.kotlin.payload.PayloadMimeType
import io.rsocket.kotlin.payload.buildPayload
import kotlinx.browser.window
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jd.benoggl.common.events.Event

@OptIn(ExperimentalMetadataApi::class)
object RSocketClient {

    private var rsocketInstance: RSocket? = null

    private val json = Json

    private suspend fun rsocket(): RSocket {
        return rsocketInstance ?: initRSocket()
    }

    suspend fun requestEventStream(): Flow<Event> {
        return rsocket().requestStream(buildPayload {
            data(ByteReadPacket.Empty)
            metadata(CompositeMetadata(RoutingMetadata("request-event-stream")))
        })
            .map { deserialize(it.data.readText()) }
    }

    private suspend fun initRSocket(): RSocket {
        val client = HttpClient {
            install(WebSockets)
            install(RSocketSupport) {
                connector = RSocketConnector {
                    connectionConfig {
                        payloadMimeType = PayloadMimeType(
                            data = WellKnownMimeType.ApplicationJson,
                            metadata = WellKnownMimeType.MessageRSocketCompositeMetadata
                        )
                    }
                }
            }
        }

        val rSocket = client.rSocket(
            host = window.location.hostname,
            port = 7000,
            path = "/rsocket"
        )
        rsocketInstance = rSocket
        return rSocket
    }

    private inline fun <reified T> deserialize(data: String): T = json.decodeFromString(data)
}
