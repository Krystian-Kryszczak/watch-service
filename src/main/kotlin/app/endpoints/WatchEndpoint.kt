package app.endpoints

import app.model.exhibit.watch.Watch
import app.service.exhibit.watch.WatchService
import app.utils.SecurityUtils
import com.datastax.oss.driver.api.core.uuid.Uuids
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Part
import io.micronaut.http.annotation.Post
import io.micronaut.http.multipart.StreamingFileUpload
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import org.slf4j.LoggerFactory
import java.util.UUID

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/watch")
class WatchEndpoint(private val watchService: WatchService) {

    @Get(consumes = [MediaType.APPLICATION_JSON])
    fun propose(authentication: Authentication?): Flowable<Watch> = watchService.propose(authentication)

    @Get("/{id}")
    fun get(id: UUID, authentication: Authentication?): Maybe<Watch> = watchService.findById(id, authentication)

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post(consumes = [MediaType.MULTIPART_FORM_DATA])
    fun add(@Part title: String, @Part description: String, @Part private: String?,
            @Part content: StreamingFileUpload, @Part miniature: StreamingFileUpload?, authentication: Authentication): Single<out HttpResponse<UUID>> {

        val clientId = SecurityUtils.getClientId(authentication)!!
        val watchId = Uuids.timeBased()
        val isPrivate = private?.toBooleanStrictOrNull() == true

        return watchService.add(
            Watch(watchId, title, clientId, description, 0, 0, isPrivate),
            content,
            miniature
        ).map {
            HttpResponse.created(watchId)
        }.onErrorReturn {
            logger.error(it.message, it.stackTrace)
            return@onErrorReturn HttpResponse.serverError()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(WatchEndpoint::class.java)
    }
}
