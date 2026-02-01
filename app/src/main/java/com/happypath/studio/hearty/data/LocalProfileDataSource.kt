package com.happypath.studio.hearty.data

import com.happypath.studio.hearty.domain.ProfileInfo
import kotlinx.coroutines.flow.Flow

interface LocalProfileDataSource {
    suspend fun addProfile(profileInfo: ProfileInfo)
    fun getProfile(): Flow<ProfileInfo?>
}