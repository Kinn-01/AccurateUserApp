package com.example.accurateuserapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.accurateuserapp.data.local.dao.UserDao
import com.example.accurateuserapp.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}