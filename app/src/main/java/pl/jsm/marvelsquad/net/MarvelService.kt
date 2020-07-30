package pl.jsm.marvelsquad.net

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelService {

    @GET("/v1/public/characters")
    suspend fun getHeroes(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): CharactersResponseModel

    @GET("/v1/public/characters/{character_id}/comics")
    suspend fun getComicsForCharacter(
        @Path("character_id") characterId: Long,
        @Query("limit") limit: Int = 100,
        @Query("orderBy") orderBy: String
    ): ComicsResponseModel

}
