package pl.jsm.marvelsquad.net.gson

import com.google.gson.JsonObject
import org.joda.time.DateTime
import java.util.*

fun JsonObject.extractThumbnail(): String {
    val thumbnailObject = getAsJsonObject("thumbnail")
    val path = thumbnailObject.get("path").asString
    val extension = thumbnailObject.get("extension").asString

    return "$path.$extension"
}

fun JsonObject.extractOnSaleDate(): Date {
    val datesArray = getAsJsonArray("dates")
    val onSaleDateString = datesArray.first {
        it.asJsonObject.get("type").asString == "onsaleDate"
    }.asJsonObject.get("date").asString

    return DateTime(onSaleDateString).toDate()
}