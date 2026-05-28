package com.example.accurateuserapp.data.WorkManager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.accurateuserapp.domain.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userRepository: UserRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val success = userRepository.syncPendingUsers()
        return if (success) {
            Result.success()
        } else {
            Result.retry()
        }
    }
}