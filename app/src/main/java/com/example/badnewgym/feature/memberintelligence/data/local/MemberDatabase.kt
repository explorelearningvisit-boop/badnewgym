package com.example.badnewgym.feature.memberintelligence.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.badnewgym.feature.memberintelligence.data.local.entities.MemberEntity

@Database(
    entities = [MemberEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MemberDatabase : RoomDatabase() {
    abstract val memberDao: MemberDao

    companion object {
        const val DATABASE_NAME = "member_intelligence_db_v3"
    }
}
