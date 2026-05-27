package com.example.accurateuserapp.domain.usecase

import com.example.accurateuserapp.domain.model.User
import com.example.accurateuserapp.domain.repository.UserRepository
import com.example.accurateuserapp.util.Resource
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User): Resource<Unit> {
        return repository.addUser(user)
    }
}