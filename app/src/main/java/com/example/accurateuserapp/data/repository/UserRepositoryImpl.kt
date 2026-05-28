package com.example.accurateuserapp.data.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.accurateuserapp.data.local.dao.UserDao
import com.example.accurateuserapp.data.mapper.toCreateRequest
import com.example.accurateuserapp.data.mapper.toDomain
import com.example.accurateuserapp.data.mapper.toEntity
import com.example.accurateuserapp.data.remote.api.ApiService
import com.example.accurateuserapp.data.WorkManager.SyncWorker
import com.example.accurateuserapp.domain.model.User
import com.example.accurateuserapp.domain.repository.UserRepository
import com.example.accurateuserapp.util.NetworkChecker
import com.example.accurateuserapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val apiService: ApiService,
    private val networkChecker: NetworkChecker,
    private val context: Context
) : UserRepository {

    private val workManager = WorkManager.getInstance(context)

    override fun getUsers(forceRefresh: Boolean): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading)

        // Expose cached data first
        val cachedUsers = userDao.getAllUsers().map { it.toDomain() }
        if (cachedUsers.isNotEmpty()) {
            emit(Resource.Success(cachedUsers))
        }

        // If force refresh or database is empty, fetch remote
        if (forceRefresh || cachedUsers.isEmpty()) {
            if (networkChecker.isNetworkAvailable()) {
                try {
                    val remoteDtos = apiService.getUsers()
                    val remoteEntities = remoteDtos.map { it.toEntity(isSynced = true) }
                    userDao.refreshRemoteUsers(remoteEntities)

                    val freshUsers = userDao.getAllUsers().map { it.toDomain() }
                    emit(Resource.Success(freshUsers))
                } catch (e: Exception) {
                    emit(Resource.Error("Gagal memperbarui data dari server: ${e.localizedMessage}", e))
                    if (cachedUsers.isNotEmpty()) {
                        emit(Resource.Success(cachedUsers))
                    }
                }
            } else {
                emit(Resource.Error("Tidak ada koneksi internet. Menampilkan data lokal.", IOException()))
                if (cachedUsers.isNotEmpty()) {
                    emit(Resource.Success(cachedUsers))
                }
            }
        }
    }

    override fun getCities(): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading)
        if (networkChecker.isNetworkAvailable()) {
            try {
                val cityResponse = apiService.getCities()
                val cities = cityResponse.mapNotNull { it["name"] }.distinct().sorted()
                emit(Resource.Success(cities))
            } catch (e: Exception) {
                emit(Resource.Success(getFallbackCities()))
            }
        } else {
            emit(Resource.Success(getFallbackCities()))
        }
    }

    override suspend fun addUser(user: User): Resource<Unit> {
        // 1. Simpan ke lokal dulu dengan status isSynced = false
        val userEntity = user.copy(isSynced = false).toEntity()
        val insertedId = userDao.insertUser(userEntity).toInt()

        // 2. Jika ada internet, coba kirim ke API
        if (networkChecker.isNetworkAvailable()) {
            try {
                // Kirim data ke server
                apiService.addUser(user.toCreateRequest())

                // Jika berhasil, update status di lokal menjadi true
                val rowsUpdated = userDao.updateSyncStatus(
                    id = insertedId,
                    isSynced = true
                )

                if (rowsUpdated == 0) {
                    // Jika query update tidak menemukan baris, coba ambil user terakhir (opsional)
                }

                return Resource.Success(Unit)

            } catch (e: Exception) {
                // Jika API gagal (meskipun internet aktif), jadwalkan via WorkManager
                enqueueSyncWork()
                // Tetap beri tahu UI bahwa data sudah tersimpan di lokal (Offline First)
                return Resource.Success(Unit)
            }
        } else {
            // 3. Jika tidak ada internet, jadwalkan sinkronisasi nanti
            enqueueSyncWork()
            return Resource.Success(Unit)
        }
    }

    override suspend fun syncPendingUsers(): Boolean {
        val unsynced = userDao.getUnsyncedUsers()
        if (unsynced.isEmpty()) return true

        var allSynced = true
        for (entity in unsynced) {
            try {
                val createRequest = entity
                    .toDomain()
                    .toCreateRequest()

                apiService.addUser(createRequest)

                userDao.updateSyncStatus(
                    id = entity.id,
                    isSynced = true
                )
            } catch (e: Exception) {
                allSynced = false
            }
        }
        return allSynced
    }

    private fun enqueueSyncWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueue(syncRequest)
    }

    private fun getFallbackCities(): List<String> {
        return listOf(
            "Aceh", "Medan", "Padang", "Jambi", "Bengkulu", "Palembang",
            "Tangerang", "Jakarta", "Bandung", "Yogyakarta", "Surabaya",
            "Malang", "Tangerang Selatan", "Pekalongan"
        )
    }
}