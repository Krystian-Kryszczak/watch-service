package app.endpoint

import app.model.exhibit.watch.Watch
import app.model.exhibit.watch.WatchModel
import app.service.exhibit.watch.WatchService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
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
    fun add(@Part watchModel: WatchModel, @Part content: StreamingFileUpload,
            @Part miniature: StreamingFileUpload?, authentication: Authentication): Single<out HttpResponse<UUID>> {


        val watch: Watch = watchModel.convert(authentication) ?: return Single.just(invalidWatchModelResp)

        return watchService.add(watch, content, miniature)
            .map { HttpResponse.created(it) }
            .defaultIfEmpty(invalidWatchModelResp)
            .onErrorReturn {
                logger.error(it.message, it.stackTrace)
                HttpResponse.serverError()
            }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(WatchEndpoint::class.java)
        private val invalidWatchModelResp = HttpResponse.status<UUID>(HttpStatus.BAD_REQUEST, "Watch model is invalid.")
    }
}
