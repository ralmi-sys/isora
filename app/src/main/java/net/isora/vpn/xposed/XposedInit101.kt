package net.isora.vpn.xposed

import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface

class XposedInit101 : XposedModule() {

    override fun onSystemServerStarting(param: XposedModuleInterface.SystemServerStartingParam) {
        HookInstaller.install(param.classLoader)
    }
}
