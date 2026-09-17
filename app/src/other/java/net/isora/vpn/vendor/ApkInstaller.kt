package net.isora.vpn.vendor

import android.content.Context
import net.isora.vpn.Application
import net.isora.vpn.bg.BoxService
import net.isora.vpn.bg.RootClient
import net.isora.vpn.database.Settings
import net.isora.vpn.utils.HookStatusClient
import net.isora.vpn.xposed.XposedActivation
import kotlinx.coroutines.delay
import java.io.File

enum class InstallMethod {
    PACKAGE_INSTALLER,
    SHIZUKU,
    ROOT,
}

object ApkInstaller {

    private suspend fun stopServiceIfRunning() {
        val commandSocket = File(Application.application.filesDir, "command.sock")
        if (!commandSocket.exists()) {
            return
        }
        BoxService.stop()
        repeat(20) {
            delay(100)
            if (!commandSocket.exists()) {
                return
            }
        }
    }

    fun getConfiguredMethod(): InstallMethod {
        if (HookStatusClient.status.value?.active == true ||
            XposedActivation.isActivated(Application.application)
        ) {
            return InstallMethod.ROOT
        }
        return if (Settings.silentInstallEnabled) {
            InstallMethod.valueOf(Settings.silentInstallMethod)
        } else {
            InstallMethod.PACKAGE_INSTALLER
        }
    }

    suspend fun install(context: Context, apkFile: File, method: InstallMethod = getConfiguredMethod()) {
        stopServiceIfRunning()
        when (method) {
            InstallMethod.SHIZUKU -> ShizukuInstaller.install(apkFile)
            InstallMethod.ROOT -> RootInstaller.install(apkFile)
            InstallMethod.PACKAGE_INSTALLER -> SystemPackageInstaller.install(context, apkFile)
        }
    }

    fun canSystemSilentInstall(): Boolean = SystemPackageInstaller.canSystemSilentInstall()

    suspend fun canSilentInstall(): Boolean {
        val method = getConfiguredMethod()
        return when (method) {
            InstallMethod.PACKAGE_INSTALLER -> canSystemSilentInstall()
            InstallMethod.SHIZUKU -> ShizukuInstaller.isAvailable() && ShizukuInstaller.checkPermission()
            InstallMethod.ROOT -> RootClient.checkRootAvailable()
        }
    }
}
