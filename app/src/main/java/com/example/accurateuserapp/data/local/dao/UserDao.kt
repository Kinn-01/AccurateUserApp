package com.example.accurateuserapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.accurateuserapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUsersFlow(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM users WHERE isSynced = 0")
    suspend fun getUnsyncedUsers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("DELETE FROM users WHERE isSynced = 1")
    suspend fun deleteSyncedUsers()

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Int)

    @Query("""
    UPDATE users
    SET isSynced = :isSynced
    WHERE id = :id
""")
    suspend fun updateSyncStatus(
        id: Int,
        isSynced: Boolean
    ): Int

    @Transaction
    suspend fun refreshRemoteUsers(remoteUsers: List<UserEntity>) {
        deleteSyncedUsers()
        insertUsers(remoteUsers)
    }
}