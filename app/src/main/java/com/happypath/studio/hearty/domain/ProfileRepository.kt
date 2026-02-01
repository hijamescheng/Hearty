package com.happypath.studio.hearty.domain

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun addProfile(profileInfo: ProfileInfo): Result<Unit>
    fun getProfile(): Flow<Result<ProfileInfo>>
}