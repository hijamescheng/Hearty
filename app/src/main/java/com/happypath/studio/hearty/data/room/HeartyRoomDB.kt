package com.happypath.studio.hearty.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MeasurementEntity::class], version = 1)
abstract class HeartyRoomDB : RoomDatabase() {
    abstract fun measurementDao(): MeasurementDao
}