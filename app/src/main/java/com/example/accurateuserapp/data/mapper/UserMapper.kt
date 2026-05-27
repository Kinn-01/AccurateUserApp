package com.example.accurateuserapp.data.mapper

import com.example.accurateuserapp.data.local.entity.UserEntity
import com.example.accurateuserapp.data.remote.dto.CreateUserRequest
import com.example.accurateuserapp.data.remote.dto.UserDto
import com.example.accurateuserapp.domain.model.User

fun UserDto.toEntity(isSynced: Boolean = true): UserEntity {
    return UserEntity(
        id = id.toIntOrNull() ?: 0,
        name = name,
        address = address,
        email = email,
        phoneNumber = phoneNumber,
        city = city,
        gender = gender,
        isSynced = isSynced
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id.toString(),
        name = name,
        address = address,
        email = email,
        phoneNumber = phoneNumber,
        city = city,
        gender = gender,
        isSynced = isSynced
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id.toIntOrNull() ?: 0,
        name = name,
        address = address,
        email = email,
        phoneNumber = phoneNumber,
        city = city,
        gender = gender,
        isSynced = isSynced
    )
}

fun User.toCreateRequest(): CreateUserRequest {
    return CreateUserRequest(
        name = name,
        address = address,
        email = email,
        phoneNumber = phoneNumber,
        city = city,
        gender = gender
    )
}