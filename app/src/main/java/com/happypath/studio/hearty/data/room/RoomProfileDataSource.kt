package com.happypath.studio.hearty.data.room

import com.happypath.studio.hearty.data.LocalProfileDataSource
import com.happypath.studio.hearty.domain.ProfileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomProfileDataSource @Inject constructor(val heartyRoomDB: HeartyRoomDB) :
    LocalProfileDataSource {
    override suspend fun addProfile(profileInfo: ProfileInfo) {
        withContext(Dispatchers.IO) {
            heartyRoomDB.profileDao().deleteAndInsertTransaction(profileInfo.toEntity())
        }
    }

    override fun getProfile(): Flow<ProfileInfo?> = heartyRoomDB.profileDao().getProfile().map {
        it?.toDomain()
    }
}