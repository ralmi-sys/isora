package net.isora.vpn.vendor

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import net.isora.vpn.Application
import net.isora.vpn.BuildConfig
import net.isora.vpn.bg.RootClient
import net.isora.vpn.database.Settings
import net.isora.vpn.utils.HookStatusClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PackageQueryManager {

    val strategy: PackageQueryStrategy
        get() = when {
            HookStatusClient.status.value?.active == true -> PackageQueryStrategy.ForcedRoot
            BuildConfig.FLAVOR == "play" -> PackageQueryStrategy.UserSelected(queryMode.value)
            else -> PackageQueryStrategy.Direct
        }

    val showModeSelector: Boolean
        get() = strategy is PackageQueryStrategy.UserSelected

    private val _queryMode = MutableStateFlow(Settings.perAppProxyPackageQueryMode)
    val queryMode: StateFlow<String> = _queryMode

    val shizukuInstalled: StateFlow<Boolean> = MutableStateFlow(false)
    val shizukuBinderReady: StateFlow<Boolean> = MutableStateFlow(false)
    val shizukuPermissionGranted: StateFlow<Boolean> = MutableStateFlow(false)
    val rootAvailable: StateFlow<Boolean?> get() = RootClient.rootAvailable
    val rootServiceConnected: StateFlow<Boolean> get() = RootClient.serviceConnected

    fun isShizukuAvailable(): Boolean = false

    fun registerListeners() {
        _queryMode.value = Settings.perAppProxyPackageQueryMode
    }

    fun unregisterListeners() {}

    fun requestShizukuPermission() {}

    fun refreshShizukuState() {}

    suspend fun checkRootAvailable(): Boolean = RootClient.checkRootAvailable()

    fun setQueryMode(mode: String) {
        _queryMode.value = mode
    }

    suspend fun getInstalledPackages(flags: Int, retryFlags: Int): List<PackageInfo> = when (val s = strategy) {
        is PackageQueryStrategy.ForcedRoot -> {
            val userId = android.os.Process.myUserHandle().hashCode()
            HookStatusClient.getInstalledPackages(Application.application, flags.toLong(), userId)
                ?: RootClient.getInstalledPackages(flags)
        }
        is PackageQueryStrategy.UserSelected -> RootClient.getInstalledPackages(flags)
        is PackageQueryStrategy.Direct -> getPackagesViaPackageManager(flags, retryFlags)
    }

    private fun getPackagesViaPackageManager(flags: Int, retryFlags: Int): List<PackageInfo> = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Application.packageManager.getInstalledPackages(
                PackageManager.PackageInfoFlags.of(flags.toLong()),
            )
        } else {
            @Suppress("DEPRECATION")
            Application.packageManager.getInstalledPackages(flags)
        }
    } catch (_: RuntimeException) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Application.packageManager.getInstalledPackages(
                PackageManager.PackageInfoFlags.of(retryFlags.toLong()),
            )
        } else {
            @Suppress("DEPRECATION")
            Application.packageManager.getInstalledPackages(retryFlags)
        }
    }
}
