package net.isora.vpn.vendor

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

/**
 * Проверка подлинности APK перед установкой (аудит 24.09, P0-03):
 * - package name обязан совпадать с нашим;
 * - подпись обязана совпадать с нашей (иначе тихая установка чужого APK);
 * - если метаданные релиза содержат sha256 — сверяем хеш файла (fail closed).
 */
object ApkVerifier {
    private const val TAG = "ApkVerifier"

    fun verifyPackage(context: Context, apkFile: File): Boolean {
        try {
            val pm = context.packageManager
            val archive = if (Build.VERSION.SDK_INT >= 28) {
                pm.getPackageArchiveInfo(apkFile.absolutePath, PackageManager.GET_SIGNING_CERTIFICATES)
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageArchiveInfo(apkFile.absolutePath, PackageManager.GET_SIGNATURES)
            } ?: run {
                Log.w(TAG, "cannot parse apk")
                return false
            }
            if (archive.packageName != context.packageName) {
                Log.w(TAG, "package mismatch: ${archive.packageName}")
                return false
            }
            if (!sameSignature(pm, context, archive)) {
                Log.w(TAG, "signature mismatch")
                return false
            }
            return true
        } catch (e: Exception) {
            Log.w(TAG, "verify failed", e)
            return false
        }
    }

    fun verifyFileHash(apkFile: File, expectedSha256: String): Boolean {
        if (expectedSha256.isBlank()) return true
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            FileInputStream(apkFile).use { input ->
                val buf = ByteArray(256 * 1024)
                while (true) {
                    val n = input.read(buf)
                    if (n <= 0) break
                    digest.update(buf, 0, n)
                }
            }
            val actual = digest.digest().joinToString("") { "%02x".format(it) }
            val ok = actual.equals(expectedSha256.trim(), ignoreCase = true)
            if (!ok) Log.w(TAG, "sha256 mismatch")
            ok
        } catch (e: Exception) {
            Log.w(TAG, "hash failed", e)
            false
        }
    }

    @SuppressLint("NewApi", "PackageManagerGetSignatures")
    private fun archiveSigners(
        pm: PackageManager,
        pkg: String?,
        archivePath: String?,
    ): List<ByteArray> {
        val info = if (archivePath != null) {
            if (Build.VERSION.SDK_INT >= 28) {
                pm.getPackageArchiveInfo(archivePath, PackageManager.GET_SIGNING_CERTIFICATES)
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageArchiveInfo(archivePath, PackageManager.GET_SIGNATURES)
            }
        } else {
            if (Build.VERSION.SDK_INT >= 28) {
                pm.getPackageInfo(pkg!!, PackageManager.GET_SIGNING_CERTIFICATES)
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageInfo(pkg!!, PackageManager.GET_SIGNATURES)
            }
        } ?: return emptyList()
        if (Build.VERSION.SDK_INT >= 28) {
            val signing = info.signingInfo ?: return emptyList()
            val arr = if (signing.hasMultipleSigners()) signing.apkContentsSigners else signing.signingCertificateHistory
            if (arr != null) return arr.map { it.toByteArray() }
            return emptyList()
        } else {
            @Suppress("DEPRECATION")
            return (info.signatures ?: return emptyList()).map { it.toByteArray() }
        }
    }

    private fun sameSignature(
        pm: PackageManager,
        context: Context,
        archive: android.content.pm.PackageInfo,
    ): Boolean {
        val archiveSigs = if (Build.VERSION.SDK_INT >= 28) {
            val signing = archive.signingInfo ?: return false
            val arr = if (signing.hasMultipleSigners()) signing.apkContentsSigners else signing.signingCertificateHistory
            (arr ?: return false).map { it.toByteArray() }
        } else {
            @Suppress("DEPRECATION")
            (archive.signatures ?: return false).map { it.toByteArray() }
        }
        if (archiveSigs.isEmpty()) return false
        val ownSigs = archiveSigners(pm, context.packageName, null)
        if (ownSigs.isEmpty()) return false
        return archiveSigs.any { a -> ownSigs.any { o -> a.contentEquals(o) } }
    }
}
