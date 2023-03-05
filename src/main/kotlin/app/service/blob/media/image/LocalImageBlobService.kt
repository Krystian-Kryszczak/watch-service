package app.service.blob.media.image

import app.service.blob.media.LocalMediaBlobService
import jakarta.inject.Singleton

@Singleton
class LocalImageBlobService: LocalMediaBlobService("image"), ImageBlobService
