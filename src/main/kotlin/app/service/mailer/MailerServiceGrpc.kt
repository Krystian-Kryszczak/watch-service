package app.service.mailer

import app.MailerReply
import app.MailerServiceGrpc.MailerServiceStub
import app.NotificationRequest
import app.model.being.user.User
import app.model.exhibit.watch.Watch
import app.service.being.user.UserService
import io.grpc.stub.StreamObserver
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class MailerServiceGrpc(private val mailerServiceStub: MailerServiceStub, private val userService: UserService): MailerService {
    private val singleFalse: Single<Boolean> = Single.just(false)
    override fun sendNewVideoNotification(address: String, video: Watch): Single<Boolean> {
        val videoName = video.name ?: return singleFalse
        val creatorId = video.creatorId ?: return singleFalse

        return userService.findByIdAsync(creatorId).map {
            user -> NotificationRequest.newBuilder()
                .setAddress(address)
                .setAuthor(formatAuthorData(user))
                .setAvatarUrl(extractAvatarUrl(user))
                .setTitle(videoName)
                .setLink(extractVideoUrl(video))
                .build()
        }.flatMapSingle { notificationRequest ->
            asObservable<MailerReply> {
                streamObserver ->  mailerServiceStub.sendNewVideoNotification(notificationRequest, streamObserver)
            }.doAfterNext {
                logger.info("new video notification has been sent to -> $address")
            }.transformToBooleanWithCatchingErrors()
        }.defaultIfEmpty(false)
    }

    private inline fun <T : Any> asObservable(crossinline body: (StreamObserver<T>) -> Unit): Observable<T> =
        Observable.create { subscription ->
            val observer = object : StreamObserver<T> {
                override fun onNext(value: T) = subscription.onNext(value)
                override fun onError(error: Throwable) = subscription.onError(error)
                override fun onCompleted() = subscription.onComplete()
            }
            body(observer)
        }
    private fun Observable<MailerReply>.transformToBooleanWithCatchingErrors() =
        firstElement()
            .map {
                it.successful
            }
            .defaultIfEmpty(false)
            .onErrorReturn {
                logger.error(it.message)
                return@onErrorReturn false
            }
    private fun formatAuthorData(user: User): String {
        var result = ""

        val userName = user.name
        if (!userName.isNullOrBlank()) result += userName

        val userLastname = user.lastname
        if (!userLastname.isNullOrBlank()) result += userLastname

        return result
    }
    private fun extractAvatarUrl(user: User): String? {
        val id = user.id
        return if (id != null) "/images/$id" else null
    }
    private fun extractVideoUrl(watch: Watch): String? {
        val id = watch.id
        return if (id != null) "/video/$id" else null
    }
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MailerServiceGrpc::class.java)
    }
}
