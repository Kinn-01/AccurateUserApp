package com.example.accurateuserapp.data.remote.api

import com.example.accurateuserapp.data.remote.dto.CreateUserRequest
import com.example.accurateuserapp.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("user")
    suspend fun getUsers(): List<UserDto>

    @POST("user")
    suspend fun addUser(@Body request: CreateUserRequest): UserDto

    @GET("city")
    suspend fun getCities(): List<Map<String, String>>
}