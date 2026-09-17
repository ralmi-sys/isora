package net.isora.vpn.update

sealed class UpdateCheckException : Exception() {
    class TrackNotSupported : UpdateCheckException()
}
