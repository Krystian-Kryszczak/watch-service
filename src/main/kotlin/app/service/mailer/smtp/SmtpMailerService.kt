package app.service.mailer.smtp

import app.model.exhibit.watch.Watch
import io.reactivex.rxjava3.core.Single

interface SmtpMailerService {
    fun sendNewVideoNotification(address: String, watch: Watch): Single<Boolean>
}
