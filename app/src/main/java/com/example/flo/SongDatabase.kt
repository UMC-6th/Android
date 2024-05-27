import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.flo.Album
import com.example.flo.AlbumDao
import com.example.flo.Song
import com.example.flo.SongDao

@Database(entities = [Song::class, Album::class], version = 2)    // version 업그레이드
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao

    companion object{
        private var instance: SongDatabase? = null

        // Migration 객체 정의
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `AlbumTable` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `artist` TEXT NOT NULL, `releaseDate` TEXT NOT NULL)")
                database.execSQL("ALTER TABLE SongTable ADD COLUMN albumIdx INTEGER")
            }
        }

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if(instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).allowMainThreadQueries()
                        .addMigrations(MIGRATION_1_2)
                        .build()
                }
            }
            return instance
        }
    }
}
