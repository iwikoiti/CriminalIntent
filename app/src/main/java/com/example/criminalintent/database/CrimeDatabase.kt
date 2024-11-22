package com.example.criminalintent.database
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.criminalintent.Crime

@Database(entities = [Crime::class], version = 2)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase:RoomDatabase() {
    abstract fun crimeDao():CrimeDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `crime_new` (
                `id` TEXT NOT NULL PRIMARY KEY,
                `title` TEXT NOT NULL,
                `date` INTEGER NOT NULL,
                `isSolved` INTEGER NOT NULL
            )
        """)
        // Переносим данные
        db.execSQL("""
            INSERT INTO crime_new (id, title, date, isSolved)
            SELECT id, title, date, isSolved FROM crime
        """)
        // Удаляем старую таблицу и переименовываем новую
        db.execSQL("DROP TABLE crime")
        db.execSQL("ALTER TABLE crime_new RENAME TO crime")
    }
}