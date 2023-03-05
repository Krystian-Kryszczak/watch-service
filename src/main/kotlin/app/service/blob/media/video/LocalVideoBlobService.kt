package app.service.blob.media.video

import app.service.blob.media.LocalMediaService
import jakarta.inject.Singleton

@Singleton
class LocalVideoService: LocalMediaService("video"), VideoService
