package app.cloud.gcp.storage

import app.storage.blob.media.MediaStorage
import app.cloud.Cloud
import com.google.cloud.storage.*
import io.micronaut.http.multipart.StreamingFileUpload
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.util.UUID

abstract class GoogleCloudStorage(private val storage: Storage, private val containerName: String): MediaStorage {
    constructor(storage: Storage, mediaType: Cloud.MediaType) : this(storage, mediaType.toString())
    override fun save(inputStream: InputStream, creatorId: UUID, private: Boolean): Single<UUID> {
        val id = UUID.randomUUID()
        return save(id, inputStream, creatorId, private).andThen(Single.just(id))
    }

    override fun save(id: UUID, inputStream: InputStream, creatorId: UUID, private: Boolean): Completable = Completable.create {
        val blobId = BlobId.of(containerName, id.toString())
        val blobInfo = BlobInfo.newBuilder(blobId)
            .setMetadata(mutableMapOf(
                "creatorId" to creatorId.toString(),
                "private" to private.toString()
            ))
            //.setContentType("text/plain")
            .build()
        storage.createFrom(blobInfo, inputStream)
        it.onComplete()
    }

    override fun save(id: UUID, fileUpload: StreamingFileUpload, creatorId: UUID, private: Boolean): Single<Boolean> = Single.create<Boolean> {
        val blobId = BlobId.of(containerName, id.toString())
        val blobInfo = BlobInfo.newBuilder(blobId)
            .setMetadata(mutableMapOf(
                "creatorId" to creatorId.toString(),
                "private" to private.toString()
            )).build()
        val writer = storage.writer(blobInfo)
        Flowable.fromPublisher(fileUpload)
            .observeOn(Schedulers.io())
            .map { partData ->
                writer.write(partData.byteBuffer)
            }.doOnComplete {
                writer.close()
            }.subscribe {
                writer.isOpen
            }
        it.onSuccess(true)
    }.onErrorReturnItem(false)

    override fun update(id: UUID, clientId: UUID, private: Boolean): Single<Boolean> = Single.create {
        val blobId = BlobId.of(containerName, id.toString())
        val blob = storage[blobId]
        val metadata = blob.metadata
        if (metadata["creatorId"]==clientId.toString()) {
            if (metadata["private"]!=private.toString()) {
                blob.toBuilder()
                    .setMetadata(metadata)
                    .build()
                    .update()
                it.onSuccess(true)
            } else {
                it.onSuccess(false)
            }
        } else {
            it.onSuccess(false)
        }
    }

    override fun update(id: UUID, clientId: UUID, inputStream: InputStream, private: Boolean?): Single<Boolean> = Single.create {
        val blobId = BlobId.of(containerName, id.toString())
        val blob = storage[blobId]
        val metadata = blob.metadata
        if (metadata["creatorId"]==clientId.toString()) {
            val writer = blob.writer()
            writer.use { channel ->
                val buffer = ByteArray(8 * 1024)
                while (inputStream.read(buffer) != -1) {
                    channel.write(ByteBuffer.wrap(buffer))
                }
                channel.close()
            }
            if (private != null) {
                if (metadata["private"]!=private.toString()) {
                    blob.toBuilder()
                        .setMetadata(metadata)
                        .build()
                        .update()
                    it.onSuccess(true)
                } else {
                    it.onSuccess(false)
                }
            } else {
                it.onSuccess(true)
            }
        } else {
            it.onSuccess(false)
        }
    }

    override fun downloadById(id: UUID): Maybe<out InputStream> = downloadById(id, null)

    override fun downloadById(id: UUID, clientId: UUID?): Maybe<out InputStream> = Maybe.create {
        val blobId = BlobId.of(containerName, id.toString())
        val blob = storage[blobId]
        val metadata = blob.metadata
        if (metadata["private"] != "true" || metadata["creatorId"] == clientId.toString()) { // if the media is public or the client is the creator of this resource
            it.onSuccess( Channels.newInputStream(blob.reader()) )
        }
        it.onComplete()
    }

    override fun deleteById(id: UUID): Completable = Completable.fromSingle(deleteByIdIfExists(id))

    override fun deleteByIdIfExists(id: UUID): Single<Boolean> = Single.create {
        val blobId = BlobId.of(containerName, id.toString())
        val blob = storage[blobId]
        it.onSuccess(blob.delete())
    }

    override fun deleteById(id: UUID, clientId: UUID): Single<Boolean> = Single.create {
        val blobId = BlobId.of(containerName, id.toString())
        val blob = storage[blobId]
        if (blob.metadata["creatorId"] == clientId.toString()) { // if the media is public or the client is the creator of this resource
            it.onSuccess(blob.delete())
        } else {
            it.onSuccess(false)
        }
    }

    override fun deleteByIdIfExists(id: UUID, clientId: UUID): Single<Boolean> = deleteById(id, clientId)
}
