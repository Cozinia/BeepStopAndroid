package com.beepstop.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

object ImageCacheHelper {
    private val client by lazy { OkHttpClient() }

    private fun safeKey(id: String): String =
        id.replace(Regex("[^a-zA-Z0-9_-]"), "_")

    fun getCachedLogo(context: Context, constructorId: String): Bitmap? {
        val key = safeKey(constructorId)
        val file = File(context.filesDir, "logo_$key.png")
        if (file.exists()) return BitmapFactory.decodeFile(file.absolutePath)

        val url = teamLogoUrl(constructorId)
        return downloadAndCache(file, url)
    }

    private fun downloadAndCache(file: File, url: String): Bitmap? {
        return try {
            val response = client.newCall(Request.Builder().url(url).build()).execute()
            val bytes = response.body?.bytes() ?: return null
            file.writeBytes(bytes)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Exception) {
            null
        }
    }

    private fun teamLogoUrl(constructorId: String): String {
        val base = "https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_180"
        return when (constructorId) {
            "red_bull"     -> "$base/common/f1/2026/redbullracing/2026redbullracinglogowhite.webp"
            "aston_martin" -> "$base/common/f1/2026/astonmartin/2026astonmartinlogowhite.webp"
            "audi"         -> "$base/common/f1/2026/audi/2026audilogowhite.webp"
            "cadillac"     -> "$base/common/f1/2026/cadillac/2026cadillaclogowhite.webp"
            else -> {
                val slug = if (constructorId == "racing_bulls") "rb" else constructorId
                "$base/content/dam/fom-website/2018-redesign-assets/team%20logos/$slug"
            }
        }
    }
}
