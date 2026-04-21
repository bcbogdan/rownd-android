package io.rownd.android.util

import android.util.Log
import io.rownd.android.models.SuperTokensAppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

suspend fun syncUserToSuperTokens(
    accessToken: String,
    appInfo: SuperTokensAppInfo,
) = withContext(Dispatchers.IO) {
    val base = "${appInfo.apiDomain}${appInfo.apiBasePath}"

    try {
        val conn = URL("$base/plugin/rownd/migrate").openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Authorization", "Bearer $accessToken")
        val code = conn.responseCode
        if (code !in 200..299) {
            Log.e("Rownd.ST", "[Rownd->ST] migrate failed with status: $code")
        }
        conn.disconnect()
    } catch (e: Exception) {
        Log.e("Rownd.ST", "[Rownd->ST] migrate failed (non-fatal): ${e.message}")
    }
}