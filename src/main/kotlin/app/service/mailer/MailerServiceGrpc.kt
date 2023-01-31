package app.service.mailer

import app.MailerReply
import app.MailerServiceGrpc.MailerServiceStub
import app.NotificationRequest
import app.model.exhibit.Exhibit
import app.service.being.user.UserService
import io.grpc.stub.StreamObserver
import io.reactivex.rxjava3.core.Single
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class MailerServiceGrpc(private val mailerServiceStub: MailerServiceStub, private val userService: UserService): MailerService {
    override fun sendNewVideoNotification(address: String, video: Exhibit): Single<Boolean> {
        if (video.name == null || video.creatorId == null || video.id == null) return Single.just(false)
        return userService.findByIdAsync(video.creatorId!!).map {
            return@map NotificationRequest.newBuilder()
                .setAddress(address)
                .setAuthor("${it.name} ${it.lastname}")
                //.setAvatarUrl(it.getAvatarUrl())
                .setTitle(video.name)
                .setLink(video.getUrl())
                .build()
        }.flatMapSingle { request ->
            Single.create<MailerReply> {
                val observer = object: StreamObserver<MailerReply> {
                    override fun onNext(value: MailerReply) = it.onSuccess(value)
                    override fun onError(t: Throwable) = it.onError(t)
                    override fun onCompleted() {}
                }
                mailerServiceStub.sendNewVideoNotification(request, observer)
            }.map { it.successful }.onErrorReturn {
                logger.error(it.message)
                return@onErrorReturn false
            }
        }.defaultIfEmpty(false)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MailerServiceGrpc::class.java)
    }
}
