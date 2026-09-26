package net.isora.vpn.update

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class UpdateInfo(
    val versionCode: Int,
    val versionName: String,
    val downloadUrl: String,
    val releaseUrl: String,
    val releaseNotes: String?,
    val isPrerelease: Boolean,
    val fileSize: Long = 0,
    // SHA-256 APK из метаданных релиза (SFA-version-metadata.json).
    // Пусто для старых релизов — тогда проверяется только подпись/пакет.
    val sha256: String = "",
) {
    fun toJson(): String = Json.encodeToString(this)

    companion object {
        fun fromJson(json: String): UpdateInfo? = runCatching {
            Json.decodeFromString<UpdateInfo>(json)
        }.getOrNull()
    }
}
