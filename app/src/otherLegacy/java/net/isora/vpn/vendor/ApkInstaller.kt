package net.isora.vpn.vendor

import android.content.Context
import net.isora.vpn.Application
import net.isora.vpn.bg.RootClient
import net.isora.vpn.database.Settings
import net.isora.vpn.utils.HookStatusClient
import net.isora.vpn.xposed.XposedActivation
import java.io.File

enum class InstallMethod {
    PACKAGE_INSTALLER,
    ROOT,
}

object ApkInstaller {

    fun getConfiguredMethod(): InstallMethod {
        if (HookStatusClient.status.value?.active == true ||
            XposedActivation.isActivated(Application.application)
        ) {
            return InstallMethod.ROOT
        }
        return if (Settings.silentInstallEnabled) {
            val method = Settings.silentInstallMethod
            if (method == "SHIZUKU") InstallMethod.ROOT else InstallMethod.valueOf(method)
        } else {
            InstallMethod.PACKAGE_INSTALLER
        }
    }

    suspend fun install(context: Context, apkFile: File, method: InstallMethod = getConfiguredMethod()) {
        when (method) {
            InstallMethod.ROOT -> RootInstaller.install(apkFile)
            InstallMethod.PACKAGE_INSTALLER -> SystemPackageInstaller.install(context, apkFile)
        }
    }

    fun canSystemSilentInstall(): Boolean = SystemPackageInstaller.canSystemSilentInstall()

    suspend fun canSilentInstall(): Boolean {
        val method = getConfiguredMethod()
        return when (method) {
            InstallMethod.PACKAGE_INSTALLER -> canSystemSilentInstall()
            InstallMethod.ROOT -> RootClient.checkRootAvailable()
        }
    }
}
