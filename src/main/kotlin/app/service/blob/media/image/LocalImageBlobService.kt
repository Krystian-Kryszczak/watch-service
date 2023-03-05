package app.service.blob.media.image

import app.service.blob.media.LocalMediaService
import jakarta.inject.Singleton

@Singleton
class LocalImageService: LocalMediaService("image"), ImageService
