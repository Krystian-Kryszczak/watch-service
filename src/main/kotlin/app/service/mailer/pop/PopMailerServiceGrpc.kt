package app.service.mailer.pop

import app.*
import app.PopMailerServiceGrpc.PopMailerServiceStub
import io.grpc.stub.StreamObserver
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import jakarta.inject.Singleton

@Singleton
class PopMailerServiceGrpc(private val popMailerServiceStub: PopMailerServiceStub): PopMailerService {
    override fun searchMessages(folder: String?, subject: String): Flowable<EmailMessage> {
        return runUsingObservable <EmailMessage>{ observer ->
            val folderAndSubject = FolderAndSubject.newBuilder()
                .setFolder(folder)
                .setSubject(subject)
                .build()
            popMailerServiceStub.searchMessages(folderAndSubject, observer)
        }
        .toFlowableWithBufferStrategy()
    }

    override fun receiveMessages(folder: String?): Flowable<EmailMessage> {
        return runUsingObservable <EmailMessage>{ observer ->
            val grpcFolder = Folder.newBuilder()
                .setFolder(folder)
                .build()
            popMailerServiceStub.receiveMessages(grpcFolder, observer)
        }
        .toFlowableWithBufferStrategy()
    }

    override fun receiveMessages(folder: String?, msgnums: IntArray): Flowable<EmailMessage> {
        return runUsingObservable <EmailMessage>{ observer ->
            val folderAndMsgNums = FolderAndMsgNums.newBuilder()
                .setFolder(folder)
                .addAllMsgnums(msgnums.asIterable())
                .build()
            popMailerServiceStub.receiveMessagesNums(folderAndMsgNums, observer)
        }
        .toFlowableWithBufferStrategy()
    }

    override fun receiveMessages(folder: String?, start: Int, end: Int): Flowable<EmailMessage> {
        return runUsingObservable <EmailMessage>{ observer ->
            val folderAndStartAndEnd = FolderAndStartAndEnd.newBuilder()
                .setFolder(folder)
                .setStart(start)
                .setEnd(end)
                .build()
            popMailerServiceStub.receiveMessagesStartEnd(folderAndStartAndEnd, observer)
        }
        .toFlowableWithBufferStrategy()
    }

    override fun receiveMessage(folder: String?, msgnum: Int): Maybe<EmailMessage> {
        return runUsingObservable <EmailMessage>{ observer ->
            val folderAndMsgNum = FolderAndMsgNum.newBuilder()
                .setFolder(folder)
                .setMsgnum(msgnum)
                .build()
            popMailerServiceStub.receiveMessage(folderAndMsgNum, observer)
        }
        .firstElement()
    }

    private inline fun <T : Any> runUsingObservable(crossinline body: (StreamObserver<T>) -> Unit): Observable<T> {
        return Observable.create {
            val observer = object : StreamObserver<T> {
                override fun onNext(value: T) = it.onNext(value)
                override fun onError(t: Throwable) = it.onError(t)
                override fun onCompleted() = it.onComplete()
            }
            body.invoke(observer)
        }
    }

    private fun <T : Any> Observable<T>.toFlowableWithBufferStrategy(): Flowable<T> = toFlowable(BackpressureStrategy.BUFFER)
}
