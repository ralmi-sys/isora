package net.isora.vpn.mirage

import android.content.Context

/**
 * Mirage sidecar: локальный SOCKS5 127.0.0.1:29183, sing-box ходит через него
 * socks-аутбаундом «🌀 Mirage» (выдача /api/singbox, только тестерам).
 *
 * Ядро — mirage.aar (gomobile из isora-junk/go/mobile). Без .aar в либах
 * старт честно отвечает ошибкой, а не роняет приложение.
 */
object MirageManager {
    const val SIDECAR_HOST = "127.0.0.1"
    const val SIDECAR_PORT = 29183
    const val SIDECAR_LISTEN = "127.0.0.1:29183"

    private const val PREFS = "mirage"
    private const val KEY_LINK = "link"
    private const val KEY_ENABLED = "enabled"

    data class Params(
        val server: String,
        val port: Int,
        val kid: String,
        val psk: String,
        val profile: String,
        val stealth: Boolean,
    )

    /** Грузит наш JNI-движок (libgojni.so). go.* классы едут из libbox,
     * поэтому явно — иначе _init негде взяться. false = движка нет. */
    private fun ensureLoaded(): Boolean {
        return try {
            System.loadLibrary("gojni")
            true
        } catch (_: Throwable) {
            // уже загружен (повторный load кидает UnsatisfiedLinkError тоже ок)
            try {
                mirage.Mirage.running()
                true
            } catch (_: Throwable) {
                false
            }
        }
    }

    /** mirage://<kid>.<psk>@<host>:<port>?profile=<p>&stealth=0|1#<name> */
    fun parseLink(link: String): Params? {
        return try {
            val body = link.removePrefix("mirage://").substringBefore("#")
            val left = body.substringBefore("@")
            val right = body.substringAfter("@")
            val kid = left.substringBefore(".")
            val psk = left.substringAfter(".")
            val host = right.substringBefore(":").substringBefore("?")
            val portAndQuery = right.substringAfter(":")
            val port = portAndQuery.substringBefore("?").toInt()
            val query = portAndQuery.substringAfter("?", "")
            val profile = query.split("&").firstOrNull { it.startsWith("profile=") }
                ?.substringAfter("=") ?: "quic"
            val stealth = query.split("&").firstOrNull { it.startsWith("stealth=") }
                ?.substringAfter("=") != "0"
            if (kid.length != 8 || psk.length != 64 || host.isEmpty()) null
            else Params(host, port, kid, psk, profile, stealth)
        } catch (_: Exception) {
            null
        }
    }

    fun saveLink(context: Context, link: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(KEY_LINK, link.trim()).apply()
    }

    fun getLink(context: Context): String? {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_LINK, null)
    }

    fun isEnabled(context: Context): Boolean {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_ENABLED, false)
    }

    fun setEnabled(context: Context, on: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_ENABLED, on).apply()
    }

    /** Блокирующий (сеть!): вызывать с IO-потока. "" = ок, иначе текст ошибки. */
    fun start(context: Context): String {
        val link = getLink(context) ?: return "Нет ссылки Mirage — вставь её в Аккаунте"
        val p = parseLink(link) ?: return "Ссылка Mirage не читается"
        return try {
            if (!ensureLoaded()) return "Ядро Mirage не вшито в сборку"
            val err = mirage.Mirage.startMirage(
                p.server, p.port.toLong(), p.kid, p.psk, p.profile, p.stealth, SIDECAR_LISTEN
            )
            if (err.isNullOrEmpty()) setEnabled(context, true)
            err ?: ""
        } catch (e: UnsatisfiedLinkError) {
            "Ядро Mirage не вшито в сборку: ${e.message}"
        } catch (e: NoClassDefFoundError) {
            "Ядро Mirage не вшито в сборку"
        } catch (e: Throwable) {
            "Mirage: ${e.message}"
        }
    }

    fun stop(context: Context): String {
        return try {
            val err = mirage.Mirage.stopMirage() ?: ""
            setEnabled(context, false)
            err
        } catch (_: Throwable) {
            setEnabled(context, false)
            ""
        }
    }

    fun running(): Boolean {
        return try {
            if (!ensureLoaded()) return false
            mirage.Mirage.running()
        } catch (_: Throwable) {
            false
        }
    }
}
