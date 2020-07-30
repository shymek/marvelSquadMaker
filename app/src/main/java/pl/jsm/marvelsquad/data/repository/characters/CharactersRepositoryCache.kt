package pl.jsm.marvelsquad.data.repository.characters

import android.content.SharedPreferences

class CharactersRepositoryCache(
    private val sharedPreferences: SharedPreferences
) {

    fun storeOffset(offset: Int) {
        sharedPreferences.edit().putInt(KEY_LAST_OFFSET, offset).apply()
    }

    fun getOffset(): Int {
        return sharedPreferences.getInt(KEY_LAST_OFFSET, 0)
    }

    fun storeLastTotal(total: Int) {
        sharedPreferences.edit().putInt(KEY_LAST_TOTAL, total).apply()
    }

    fun getLastTotal(): Int {
        return sharedPreferences.getInt(KEY_LAST_TOTAL, 0)
    }

    companion object {
        private const val KEY_LAST_OFFSET = "charactersLastOffset"
        private const val KEY_LAST_TOTAL = "charactersLastTotal"
    }

}