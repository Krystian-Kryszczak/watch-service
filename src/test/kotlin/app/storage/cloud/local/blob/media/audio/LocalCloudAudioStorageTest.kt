package app.storage.cloud.local.blob.media.audio

import app.cloud.local.blob.media.audio.LocalAudioStorage
import io.kotest.core.spec.style.StringSpec
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

@MicronautTest
class LocalCloudAudioStorageTest(localAudioStorage: LocalAudioStorage): StringSpec({

    suspend fun saveAndDownloadTest(private: Boolean): Boolean {
        val randomCreatorId = UUID.randomUUID()
        val tempFile = File.createTempFile("upload", "test")
        val testText = "Hello world!"
        tempFile.writeText(testText)
        val inputStream = tempFile.inputStream()
        return withContext(Dispatchers.IO) {
            localAudioStorage.save(inputStream, randomCreatorId, private)
                .flatMapMaybe { localAudioStorage.downloadById(it, randomCreatorId) }
                .observeOn(Schedulers.io())
                .map {
                    val newFile = File.createTempFile("output", "test")

                    it.use { input ->
                        newFile.outputStream().use { output ->
                            input.transferTo(output)
                        }
                    }

                    val content = newFile.readLines().firstOrNull()
                    println("Test text: $testText")
                    println("File content: $content")
                    val result = content == testText
                    result
                }.defaultIfEmpty(false)
                .blockingGet()
        }
    }

    "test successful save and download private file content" {
        assert(saveAndDownloadTest(true))
    }

    "test successful save and download public file content" {
        assert(saveAndDownloadTest(false))
    }
})
