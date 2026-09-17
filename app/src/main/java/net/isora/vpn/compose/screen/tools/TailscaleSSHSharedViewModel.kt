package net.isora.vpn.compose.screen.tools

import net.isora.vpn.compose.base.BaseViewModel
import net.isora.vpn.terminal.TailscaleSSHPresentedSession

data class TailscaleSSHSharedState(
    val pendingSession: TailscaleSSHPresentedSession? = null,
)

class TailscaleSSHSharedViewModel : BaseViewModel<TailscaleSSHSharedState, Nothing>() {
    override fun createInitialState() = TailscaleSSHSharedState()

    fun setPendingSession(session: TailscaleSSHPresentedSession) {
        updateState { copy(pendingSession = session) }
    }

    fun consumePendingSession(): TailscaleSSHPresentedSession? {
        val session = currentState.pendingSession
        updateState { copy(pendingSession = null) }
        return session
    }
}
