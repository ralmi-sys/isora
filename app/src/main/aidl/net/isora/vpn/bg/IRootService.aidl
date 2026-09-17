package net.isora.vpn.bg;

import android.os.ParcelFileDescriptor;
import net.isora.vpn.bg.IAutoRedirectHandler;
import net.isora.vpn.bg.IAutoRedirectSession;
import net.isora.vpn.bg.IBridgeSession;
import net.isora.vpn.bg.INeighborTableCallback;
import net.isora.vpn.bg.IRootShellSession;
import net.isora.vpn.bg.ParceledListSlice;

interface IRootService {
    void destroy() = 16777114; // Destroy method defined by Shizuku server

    ParceledListSlice getInstalledPackages(int flags, int userId) = 1;

    void installPackage(in ParcelFileDescriptor apk, long size, int userId) = 2;

    String exportDebugInfo(String outputPath) = 3;

    void registerNeighborTableCallback(in INeighborTableCallback callback) = 4;

    oneway void unregisterNeighborTableCallback(in INeighborTableCallback callback) = 5;

    IRootShellSession openShellSession(String user, String command, in String[] env, String term, int rows, int cols) = 6;

    String lookupSFTPServer() = 7;

    IBridgeSession openBridge(String bridgeName, int mtu, String inet4Port, String inet6Port, int ruleIndex, int routeTable) = 8;

    IAutoRedirectSession startAutoRedirect(in byte[] options, IAutoRedirectHandler handler) = 9;
}
