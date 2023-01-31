package app.cloud.azure

import app.cloud.Cloud
import jakarta.inject.Singleton

@Singleton
class Azure(override val name: String = "Azure") : Cloud
