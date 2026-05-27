package com.example.accurateuserapp.domain.repository

import com.example.accurateuserapp.domain.model.User
import com.example.accurateuserapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(forceRefresh: Boolean): Flow<Resource<List<User>>>
    fun getCities(): Flow<Resource<List<String>>>
    suspend fun addUser(user: User): Resource<Unit>
    suspend fun syncPendingUsers(): Boolean
}