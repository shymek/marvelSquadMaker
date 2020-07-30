package pl.jsm.marvelsquad.net

import okhttp3.Interceptor
import okhttp3.Response
import pl.jsm.marvelsquad.BuildConfig
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        val timestamp = getTimestamp()
        val url = req.url.newBuilder()
            .addQueryParameter("apikey", BuildConfig.API_KEY)
            .addQueryParameter("hash", calculateHash(timestamp))
            .addQueryParameter("ts", timestamp)
            .build()
        req = req.newBuilder().url(url).build()

        return chain.proceed(req)
    }

    private fun getTimestamp(): String {
        return URLEncoder.encode(Date().toString(), "utf-8")
    }

    private fun calculateHash(timestamp: String): String {
        return (timestamp + BuildConfig.API_PRIVATE_KEY + BuildConfig.API_KEY).md5()
    }

    private fun String.md5(): String {
        val md5 = "MD5"
        try {
            // Create MD5 Hash
            val digest = MessageDigest
                .getInstance(md5)
            digest.update(this.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }
}
