package app.service.mailer

import app.model.exhibit.watch.Watch
import io.reactivex.rxjava3.core.Single

interface MailerService {
    fun sendNewVideoNotification(address: String, video: Watch): Single<Boolean>
}
