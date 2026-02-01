package com.happypath.studio.hearty.domain

import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(val repository: ProfileRepository) {
    suspend operator fun invoke(profileInfo: ProfileInfo){
        repository.addProfile(profileInfo)
    }
}