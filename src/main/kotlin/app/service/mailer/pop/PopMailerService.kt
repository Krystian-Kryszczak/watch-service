package app.service.mailer.pop

import app.EmailMessage
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe

interface PopMailerService {
    fun searchMessages(folder: String? = null, subject: String): Flowable<EmailMessage>
    fun receiveMessages(folder: String? = null): Flowable<EmailMessage>
    fun receiveMessages(folder: String? = null, msgnums: IntArray): Flowable<EmailMessage>
    fun receiveMessages(folder: String? = null, start: Int, end: Int): Flowable<EmailMessage>
    fun receiveMessage(folder: String? = null, msgnum: Int): Maybe<EmailMessage>
}
