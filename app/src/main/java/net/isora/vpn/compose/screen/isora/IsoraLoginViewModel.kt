package net.isora.vpn.compose.screen.isora

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.nekohasekai.libbox.Libbox
import net.isora.vpn.bg.UpdateProfileWork
import net.isora.vpn.database.Profile
import net.isora.vpn.database.ProfileManager
import net.isora.vpn.database.TypedProfile
import net.isora.vpn.utils.HTTPClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date

/** Вход через Telegram: код → Start в боте → готовая подписка. */
class IsoraLoginViewModel(application: Application) : AndroidViewModel(application) {

    enum class Phase { Idle, Waiting, Done, Error }

    data class LoginUiState(
        val phase: Phase = Phase.Idle,
        val botUrl: String? = null,
        val error: String? = null,
    )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private var pollJob: Job? = null

    fun startLogin() {
        if (pollJob?.isActive == true) return
        _uiState.update { it.copy(phase = Phase.Waiting, error = null, botUrl = null) }
        pollJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val (code, botUrl) = postStart()
                _uiState.update { it.copy(botUrl = botUrl) }
                var subUrl: String? = null
                var tries = 0
                while (subUrl == null && tries < 100) {
                    delay(3000)
                    tries++
                    subUrl = pollCode(code)
                }
                val url = subUrl ?: throw Exception("Время вышло — нажми ещё раз")
                importSubscription(url)
                _uiState.update { it.copy(phase = Phase.Done) }
                importSubscription(url)
                _uiState.update { it.copy(phase = Phase.Done) }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) throw e
                _uiState.update { it.copy(phase = Phase.Error, error = e.message ?: "Не вышло") }
            }
        }
    }

    fun cancel() {
        pollJob?.cancel()
        pollJob = null
        _uiState.update { LoginUiState() }
    }

    private fun apiBase(): String = "https://isora.duckdns.org:8443"

    private fun postStart(): Pair<String, String> {
        val conn = (URL(apiBase() + "/api/app-login/start").openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 15000
            readTimeout = 15000
            doOutput = true
        }
        try {
            conn.connect()
            val body = conn.inputStream.bufferedReader().readText()
            if (conn.responseCode !in 200..299) throw Exception("Сервер молчит (${conn.responseCode})")
            val json = JSONObject(body)
            return Pair(json.getString("code"), json.getString("botUrl"))
        } finally {
            conn.disconnect()
        }
    }

    /** null = ещё ждём; иначе готовая singbox-ссылка. */
    private fun pollCode(code: String): String? {
        val conn = (URL(apiBase() + "/api/app-login/" + code).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 15000
            readTimeout = 15000
        }
        try {
            return when (conn.responseCode) {
                200 -> JSONObject(conn.inputStream.bufferedReader().readText()).getString("subUrl")
                202 -> null
                else -> throw Exception("Код протух — начни заново")
            }
        } finally {
            conn.disconnect()
        }
    }

    private suspend fun importSubscription(subUrl: String) {
        val context = getApplication<Application>()
        val content = HTTPClient().use { it.getString(subUrl) }
        Libbox.checkConfig(content)
        val typedProfile = TypedProfile().apply {
            type = TypedProfile.Type.Remote
            remoteURL = subUrl
            autoUpdate = true
            autoUpdateInterval = 60
            lastUpdated = Date()
        }
        val profile = Profile(name = "ISORA", typed = typedProfile).apply {
            userOrder = ProfileManager.nextOrder()
        }
        val fileID = ProfileManager.nextFileID()
        val configFile = File(File(context.filesDir, "configs").also { it.mkdirs() }, "$fileID.json")
        typedProfile.path = configFile.path
        configFile.writeText(content)
        ProfileManager.create(profile, andSelect = true)
        runCatching { UpdateProfileWork.reconfigureUpdater() }
    }
}
