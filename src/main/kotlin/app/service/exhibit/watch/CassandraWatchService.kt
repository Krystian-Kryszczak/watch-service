package app.service.exhibit.watch

import app.model.exhibit.watch.Watch
import app.service.being.user.UserService
import app.service.blob.media.image.ImageBlobService
import app.service.blob.media.video.VideoBlobService
import app.service.exhibit.AbstractExhibitService
import app.storage.cassandra.dao.exhibit.watch.WatchDao
import app.utils.SecurityUtils
import io.micronaut.http.multipart.StreamingFileUpload
import io.micronaut.security.authentication.Authentication
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import jakarta.inject.Named
import jakarta.inject.Singleton
import java.util.UUID

@Singleton
class CassandraWatchService(
    private val watchDao: WatchDao,
    private val userService: UserService,
    @Named("local") private val videoBlobService: VideoBlobService,
    @Named("local") private val imageBlobService: ImageBlobService
): WatchService, AbstractExhibitService<Watch>(watchDao) {
    override fun propose(authentication: Authentication?): Flowable<Watch> = Flowable.fromPublisher(watchDao.findReactive(10)).flatMapSingle { watch ->
        val creatorId = watch.creatorId
        return@flatMapSingle if (creatorId != null) userService.findByIdAsync(creatorId).map { user ->
            watch.creatorName = (user.name ?: "·") + " " + (user.lastname ?: "·")
            return@map watch
        }.defaultIfEmpty(watch)
        else {
            Single.just(watch)
        }
    }
    override fun findById(id: UUID, authentication: Authentication?): Maybe<Watch> =
        findById(id)
        .filter {
            !it.private || SecurityUtils.clientIsCreator(it.creatorId, authentication)
        }
        .flatMap { watch ->
            val creatorId = watch.creatorId
            if (creatorId != null) userService.findByIdAsync(creatorId).map { user ->
                watch.creatorName = (user.name ?: "·") + " " + (user.lastname ?: "·")
                return@map watch
            } else Maybe.empty()
        }
    override fun add(item: Watch, content: StreamingFileUpload, miniature: StreamingFileUpload?): Single<Boolean> {
        if (item.id == null || item.creatorId == null) return Single.just(false)
        return save(item)
            .andThen(videoBlobService.save(item.id!!, content, item.creatorId!!, item.private))
            .flatMap {
                if (miniature != null) imageBlobService.save(item.id!!, miniature, item.creatorId!!, item.private)
                else Single.just(it)
            }
    }
    override fun deleteById(id: UUID): Completable = Completable.fromPublisher(watchDao.deleteByIdReactive(id))
    override fun deleteByIdIfExists(id: UUID): Single<Boolean> = Single.fromPublisher(watchDao.deleteByIdIfExistsReactive(id))
}
