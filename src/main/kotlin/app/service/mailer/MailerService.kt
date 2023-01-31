package app.service.mailer

import app.model.exhibit.Exhibit
import io.reactivex.rxjava3.core.Single

interface MailerService {
    fun sendNewVideoNotification(address: String, video: Exhibit): Single<Boolean>
}
