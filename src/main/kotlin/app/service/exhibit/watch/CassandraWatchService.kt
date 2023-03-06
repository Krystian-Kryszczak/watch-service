package app.service.exhibit.watch

import app.model.exhibit.watch.Watch
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
import jakarta.inject.Singleton
import java.util.UUID

@Singleton
class CassandraWatchService(
    private val watchDao: WatchDao,
    private val videoBlobService: VideoBlobService,
    private val imageBlobService: ImageBlobService
): WatchService, AbstractExhibitService<Watch>(watchDao) {

    override fun propose(authentication: Authentication?): Flowable<Watch> = Flowable.fromPublisher(watchDao.findReactive(10))

    override fun findById(id: UUID, authentication: Authentication?): Maybe<Watch> =
        findById(id).filter { it.canUserSeeIt(authentication) }

    private fun Watch.canUserSeeIt(authentication: Authentication?): Boolean = !private || SecurityUtils.clientIsCreator(creatorId, authentication)

    override fun add(item: Watch, content: StreamingFileUpload, miniature: StreamingFileUpload?): Maybe<UUID> {
        if (item.id == null || item.creatorId == null) return Maybe.empty()
        return save(item)
            .andThen(videoBlobService.save(item.id, content, item.creatorId, item.private))
            .flatMapMaybe {
                if (miniature != null) {
                    imageBlobService.save(item.id, miniature, item.creatorId, item.private)
                    .flatMapMaybe { Maybe.just(item.id) }
                } else {
                    Maybe.empty()
                }
            }
    }

    override fun deleteById(id: UUID): Completable = Completable.fromPublisher(watchDao.deleteByIdReactive(id))

    override fun deleteByIdIfExists(id: UUID): Single<Boolean> = Single.fromPublisher(watchDao.deleteByIdIfExistsReactive(id))
}
