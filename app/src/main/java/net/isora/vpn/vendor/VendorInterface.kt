package net.isora.vpn.vendor

import android.app.Activity
import androidx.camera.core.ImageAnalysis
import net.isora.vpn.compose.screen.qrscan.QRCodeCropArea
import net.isora.vpn.update.UpdateInfo
import net.isora.vpn.update.UpdateSource

interface VendorInterface {
    fun checkUpdate(activity: Activity, byUser: Boolean)

    fun createQRCodeAnalyzer(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit,
        onCropArea: ((QRCodeCropArea?) -> Unit)? = null,
    ): ImageAnalysis.Analyzer?

    fun isPerAppProxyAvailable(): Boolean = true

    val hasCustomUpdate: Boolean get() = false

    val updateSources: List<UpdateSource> get() = listOf(UpdateSource.GITHUB)

    fun checkUpdateAsync(): UpdateInfo? = null

    fun scheduleAutoUpdate() {}

    suspend fun verifySilentInstallMethod(method: String): Boolean = false

    suspend fun downloadAndInstall(context: android.content.Context, downloadUrl: String): Unit = throw UnsupportedOperationException("Not supported in this flavor")
}
