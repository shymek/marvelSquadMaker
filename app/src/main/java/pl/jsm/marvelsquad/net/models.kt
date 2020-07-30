package pl.jsm.marvelsquad.net

import com.google.gson.annotations.SerializedName
import java.util.*

data class CharacterDtoModel(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnail") val thumbnail: String
)

data class CharactersResponseDataModel(
    @SerializedName("offset") val offset: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("results") val characters: List<CharacterDtoModel>
)

data class CharactersResponseModel(
    @SerializedName("data") val data: CharactersResponseDataModel
)

data class ComicDtoModel(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("onSaleDate") val onSaleDate: Date
)

data class ComicsResponseDataModel(
    @SerializedName("offset") val offset: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("results") val comics: List<ComicDtoModel>
)

data class ComicsResponseModel(
    @SerializedName("data") val data: ComicsResponseDataModel
)
