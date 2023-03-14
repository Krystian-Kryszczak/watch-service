package app.endpoint

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.annotation.Client
import io.micronaut.rxjava3.http.client.Rx3HttpClient
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

@MicronautTest
class WatchEndpointTest(@Client("/") private val rx3HttpClient: Rx3HttpClient) : StringSpec({

    "watch propose should return any list" {
        withContext(Dispatchers.IO) {
            rx3HttpClient.exchange(
                HttpRequest.GET<List<UUID>>("/watch")
            ).blockingFirst()
        } shouldNotBe null
    }

    "get public watch should return watch" {
        // TODO
    }

    "get private watch should return null" {
        // TODO
    }

    "get private watch with owner authentication should return watch" {
        // TODO
    }

    "get private watch with non-owner authentication should return null" {
        // TODO
    }

    "add without authentication should return null" {
        // TODO
    }

    "add with authentication should return added watch uuid" {
        // TODO
    }
})
