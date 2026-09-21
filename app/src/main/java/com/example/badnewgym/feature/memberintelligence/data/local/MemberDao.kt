package com.example.badnewgym.feature.memberintelligence.data.local

import androidx.room.*
import com.example.badnewgym.feature.memberintelligence.data.local.entities.MemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Query("SELECT * FROM members WHERE id = :id")
    fun getMemberById(id: String): MemberEntity?

    @Query("SELECT * FROM members WHERE gymId = :gymId")
    fun getMembersStream(gymId: String): Flow<List<MemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMember(member: MemberEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMembers(members: List<MemberEntity>): List<Long>

    @Query("DELETE FROM members WHERE id = :id")
    fun deleteMember(id: String): Int
    
    @Query("DELETE FROM members")
    fun clearAll(): Int
}
