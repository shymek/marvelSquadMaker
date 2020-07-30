package pl.jsm.marvelsquad.net.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import pl.jsm.marvelsquad.net.CharacterDtoModel
import java.lang.reflect.Type

class CharacterDeserializer : JsonDeserializer<CharacterDtoModel> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CharacterDtoModel {
        val jsonObject = json.asJsonObject

        return CharacterDtoModel(
            jsonObject.get("id").asLong,
            jsonObject.get("name").asString,
            jsonObject.get("description").asString,
            jsonObject.extractThumbnail()
        )
    }
}
