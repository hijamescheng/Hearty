package com.happypath.studio.hearty.di

import com.happypath.studio.hearty.data.LocalMeasurementDataSource
import com.happypath.studio.hearty.data.MeasurementRepositoryImpl
import com.happypath.studio.hearty.data.room.RoomMeasurementDataSource
import com.happypath.studio.hearty.domain.MeasurementRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BinderModule {
    @Binds
    abstract fun bindLocalMovieDataSource(roomMovieDataSource: RoomMeasurementDataSource): LocalMeasurementDataSource

    @Binds
    abstract fun bindMeasurementRepository(measurementRepositoryImpl: MeasurementRepositoryImpl): MeasurementRepository
}
