package au.com.hearty.data

import android.content.Context
import androidx.room.Room

class DatabaseFactory {

    companion object {

        @Volatile
        private lateinit var INSTANCE: HeartyRoomDatabase

        fun getDatabase(context: Context): HeartyRoomDatabase {
            if (!::INSTANCE.isInitialized) {
                synchronized(HeartyRoomDatabase::class.java) {
                    if (!::INSTANCE.isInitialized) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            HeartyRoomDatabase::class.java, DBName
                        ).build()
                    }
                }
            }
            return INSTANCE
        }
    }
}