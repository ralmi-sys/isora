package net.isora.vpn.vendor

import android.content.pm.PackageInfo
import android.os.ParcelFileDescriptor
import net.isora.vpn.bg.IShizukuService
import net.isora.vpn.bg.ParceledListSlice
import java.io.IOException

class ShizukuPrivilegedService : IShizukuService.Stub() {

    override fun destroy() {
        System.exit(0)
    }

    override fun getInstalledPackages(flags: Int, userId: Int): ParceledListSlice<PackageInfo> {
        val allPackages = PrivilegedServiceUtils.getInstalledPackages(flags, userId)
        return ParceledListSlice(allPackages)
    }

    override fun installPackage(apk: ParcelFileDescriptor?, size: Long, userId: Int) {
        if (apk == null) throw IOException("APK file descriptor is null")
        PrivilegedServiceUtils.installPackage(apk, size, userId)
    }
}
