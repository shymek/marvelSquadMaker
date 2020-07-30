package pl.jsm.marvelsquad.data

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Character(
    @PrimaryKey val characterId: Long = 0,
    val name: String = "",
    val description: String = "",
    val thumbnailUrl: String = "",
    val isInSquad: Boolean = false
) : Parcelable

@Entity
data class Comic(
    @PrimaryKey val comicId: Long,
    val title: String,
    val thumbnailUrl: String,
    val onSaleDate: Date
)

@Entity(
    primaryKeys = ["comicId", "characterId"],
    indices = [Index("comicId"), Index("characterId")]
)
data class ComicsCharacterCrossRef(
    val comicId: Long,
    val characterId: Long
)

data class CharactersComics(
    @Embedded val character: Character,
    @Relation(
        parentColumn = "characterId",
        entityColumn = "comicId",
        associateBy = Junction(ComicsCharacterCrossRef::class)
    )
    val comics: List<Comic>
)
