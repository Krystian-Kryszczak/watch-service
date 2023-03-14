package app.endpoint.media

import app.service.blob.media.audio.AudioBlobBlobService
import app.service.blob.media.image.ImageBlobService
import app.service.blob.media.video.VideoBlobService
import com.datastax.oss.driver.api.core.uuid.Uuids
import io.kotest.core.spec.style.StringSpec
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import jakarta.inject.Named
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

@MicronautTest
class MediaControllersTest(
    @Named("local")
    private val videoBlobService: VideoBlobService,
    @Named("local")
    private val imageBlobService: ImageBlobService,
    @Named("local")
    private val audioBlobService: AudioBlobBlobService
): StringSpec({

    val basePath = "./src/test/resources/media"
    val videoPath = "$basePath/video/world.mp4"
    val imagePath = "$basePath/image/java.png"
    val audioPath = "$basePath/audio/test.wav"

    "video controller GET endpoint test" {
        val file = File(videoPath)
        val randomCreatorId = UUID.randomUUID()
        val savedBlobId =  videoBlobService.save(file.inputStream(), randomCreatorId, false).blockingGet()
//        spec
//            .`when`().get("/video/$savedBlobId")
            //.then().statusCode(200)
            //.body(`is`(file.readBytes()))
    }

    "image controller GET endpoint test" {
        val file = File(imagePath)
        val randomCreatorId = Uuids.timeBased()
        val savedBlobId = withContext(Dispatchers.IO) {
            imageBlobService.save(file.inputStream(), randomCreatorId, false).blockingGet()
        }
//        spec
//            .`when`().get("/images/$savedBlobId")
//            .then().statusCode(200)
//            .body(`is`(file.readBytes()))
    }

    "audio controller GET endpoint est" {
        val file = File(audioPath)
        val randomCreatorId = Uuids.timeBased()
        val savedBlobId = withContext(Dispatchers.IO) {
            audioBlobService.save(file.inputStream(), randomCreatorId, false).blockingGet()
        }
//        spec
//            .`when`().get("/audio/$savedBlobId")
//            .then().statusCode(200)
//            .body(`is`(file.readBytes()))
    }
})
