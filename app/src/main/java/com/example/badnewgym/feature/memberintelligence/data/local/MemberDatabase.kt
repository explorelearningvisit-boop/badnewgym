package com.example.badnewgym.feature.memberintelligence.data.local

import androidx.room.Database
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.badnewgym.feature.memberintelligence.data.local.entities.MemberEntity

@Database(
    entities = [MemberEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MemberDatabase : RoomDatabase() {
    abstract val memberDao: MemberDao

    companion object {
        const val DATABASE_NAME = "member_intelligence_db_v3"

        /**
         * Additive migration for the optional engagement JSON column.
         * Existing member records remain valid with a null engagement value.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE members ADD COLUMN engagement TEXT")
            }
        }
    }
}
