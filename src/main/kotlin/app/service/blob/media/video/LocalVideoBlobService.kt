package app.service.blob.media.video

import app.service.blob.media.LocalMediaBlobService
import jakarta.inject.Singleton

@Singleton
class LocalVideoBlobService: LocalMediaBlobService("video"), VideoBlobService
