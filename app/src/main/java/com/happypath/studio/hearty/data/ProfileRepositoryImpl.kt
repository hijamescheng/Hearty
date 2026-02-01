package com.happypath.studio.hearty.data

import androidx.sqlite.SQLiteException
import com.happypath.studio.hearty.domain.ProfileInfo
import com.happypath.studio.hearty.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    val localProfileDataSource: LocalProfileDataSource
) : ProfileRepository {
    override suspend fun addProfile(profileInfo: ProfileInfo): Result<Unit> =
        try {
            localProfileDataSource.addProfile(profileInfo)
            Result.success(Unit)
        } catch (e: SQLiteException) {
            Result.failure(e)
        }

    override fun getProfile(): Flow<Result<ProfileInfo>> {
        return localProfileDataSource.getProfile().map { result ->
            result?.let {
                Result.success(result)
            } ?: Result.failure(NoProfileFoundException)
        }
    }
}

object NoProfileFoundException : Exception() {
    private fun readResolve(): Any = NoProfileFoundException
}
