package pl.jsm.marvelsquad.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.data.Comic
import pl.jsm.marvelsquad.data.ComicsCharacterCrossRef
import pl.jsm.marvelsquad.data.db.dao.CharacterDao
import pl.jsm.marvelsquad.data.db.dao.ComicDao

@Database(
    entities = [Character::class, Comic::class, ComicsCharacterCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun comicDao(): ComicDao
}