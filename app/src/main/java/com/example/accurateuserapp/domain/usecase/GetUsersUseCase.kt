package com.example.accurateuserapp.domain.usecase

import com.example.accurateuserapp.domain.model.User
import com.example.accurateuserapp.domain.repository.UserRepository
import com.example.accurateuserapp.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(forceRefresh: Boolean = false): Flow<Resource<List<User>>> {
        return repository.getUsers(forceRefresh)
    }
}