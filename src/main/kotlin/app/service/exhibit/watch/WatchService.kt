package app.service.exhibit.watch

import app.model.exhibit.watch.Watch
import app.service.exhibit.ExhibitService
import io.micronaut.http.multipart.StreamingFileUpload
import io.reactivex.rxjava3.core.Maybe
import java.util.UUID

interface WatchService: ExhibitService<Watch> {
    fun add(item: Watch, content: StreamingFileUpload, miniature: StreamingFileUpload?): Maybe<UUID>
}
