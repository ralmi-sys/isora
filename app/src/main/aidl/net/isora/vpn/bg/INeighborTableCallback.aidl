package net.isora.vpn.bg;

import net.isora.vpn.bg.ParceledListSlice;

interface INeighborTableCallback {
    oneway void onNeighborTableUpdated(in ParceledListSlice entries);
}
