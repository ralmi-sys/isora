package net.isora.vpn.bg;

import android.os.ParcelFileDescriptor;
import net.isora.vpn.bg.ParceledListSlice;

interface IShizukuService {
    void destroy() = 16777114; // Destroy method defined by Shizuku server

    ParceledListSlice getInstalledPackages(int flags, int userId) = 1;

    void installPackage(in ParcelFileDescriptor apk, long size, int userId) = 2;
}
