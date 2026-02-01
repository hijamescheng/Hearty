package com.happypath.studio.hearty.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.happypath.studio.hearty.domain.ProfileInfo

@Entity
data class ProfileEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val height: Int,
    val weight: Int,
    val birthday: Long,
    val sex: Int
)

fun ProfileInfo.toEntity() = ProfileEntity(
    height = height,
    weight = weight,
    birthday = birthday,
    sex = sex
)

fun ProfileEntity.toDomain() = ProfileInfo(
    height = height,
    weight = weight,
    birthday = birthday,
    sex = sex
)