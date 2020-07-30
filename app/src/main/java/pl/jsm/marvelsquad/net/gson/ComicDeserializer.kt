package pl.jsm.marvelsquad.net.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import pl.jsm.marvelsquad.net.ComicDtoModel
import java.lang.reflect.Type

class ComicDeserializer : JsonDeserializer<ComicDtoModel> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ComicDtoModel {
        val jsonObject = json.asJsonObject

        return ComicDtoModel(
            jsonObject.get("id").asLong,
            jsonObject.get("title").asString,
            jsonObject.extractThumbnail(),
            jsonObject.extractOnSaleDate()
        )
    }
}
