package com.happypath.studio.hearty.domain

import com.happypath.studio.hearty.data.ProfileRepositoryImpl
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(val repositoryImpl: ProfileRepositoryImpl) {
      operator fun invoke() = repositoryImpl.getProfile()
}