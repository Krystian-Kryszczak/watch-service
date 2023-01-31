package app.cloud.gcp.service.media.image

import app.service.blob.media.AbstractMediaBlobService
import app.service.blob.media.image.ImageBlobService
import app.storage.blob.media.image.ImageStorage
import jakarta.inject.Named
import jakarta.inject.Singleton

@Singleton
class GoogleCloudImageBlobService(@Named("googlecloud") imageStorage: ImageStorage): AbstractMediaBlobService(imageStorage), ImageBlobService
