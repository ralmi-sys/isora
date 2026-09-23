package net.isora.vpn.compose.screen.isora

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

/**
 * Честные глаза из приложения для Дозора (server/pingwatch.ts):
 * живые пинги urltest по странам. Только цифры (как сайт), IP/юзеров не шлём.
 * Троттлинг 10 мин. Замеры сквозь чужой VPN всё равно кривые — но это видно
 * по картине в целом, а наши exit-IP бэкенд и так отбрасывает.
 */
object PingReporter {
    private const val PREFS = "isora_ui"
    private const val KEY_LAST = "ping_sent_at"
    private const val INTERVAL_MS = 10 * 60 * 1000L
    private const val ENDPOINT = "https://isora.duckdns.org:8443/api/ping-report"

    private fun countryOf(tag: String): String? = when {
        tag.contains("NL") -> "NL"
        tag.contains("DE") -> "DE"
        tag.contains("FI") -> "FI"
        tag.contains("SE") -> "SE"
        tag.contains("FR") -> "FR"
        else -> null
    }

    suspend fun maybeReport(context: Context, delays: Map<String, Int>) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (System.currentTimeMillis() - prefs.getLong(KEY_LAST, 0) < INTERVAL_MS) return
        val best = mutableMapOf<String, Int>()
        for ((tag, ms) in delays) {
            if (ms <= 0 || ms > 10000) continue // лимит сервера: 0..10000, иначе весь отчёт в брак
            val c = countryOf(tag) ?: continue
            val prev = best[c]
            if (prev == null || ms < prev) best[c] = ms
        }
        if (best.isEmpty()) return
        if (post(best)) prefs.edit().putLong(KEY_LAST, System.currentTimeMillis()).apply()
    }

    private suspend fun post(best: Map<String, Int>): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val body = buildString {
                append("{\"pings\":{")
                append(
                    listOf("NL", "DE", "FI", "SE", "FR").joinToString(",") { k ->
                        "\"$k\":" + (best[k]?.toString() ?: "null")
                    }
                )
                append("},\"src\":\"app\"}")
            }
            val conn = (URL(ENDPOINT).openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("User-Agent", "ISORA-App/1.0")
                connectTimeout = 10000
                readTimeout = 10000
                doOutput = true
            }
            conn.outputStream.use { it.write(body.toByteArray()) }
            val code = conn.responseCode
            conn.disconnect()
            code in 200..299
        }.getOrDefault(false)
    }
}
