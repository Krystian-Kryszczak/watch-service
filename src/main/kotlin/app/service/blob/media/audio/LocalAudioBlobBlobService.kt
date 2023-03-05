package app.service.blob.media.audio

import app.service.blob.media.LocalMediaBlobService
import jakarta.inject.Singleton

@Singleton
class LocalAudioBlobBlobService: LocalMediaBlobService("audio"), AudioBlobBlobService
