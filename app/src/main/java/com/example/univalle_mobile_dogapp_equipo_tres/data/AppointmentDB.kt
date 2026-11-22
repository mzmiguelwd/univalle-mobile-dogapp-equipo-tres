package com.example.univalle_mobile_dogapp_equipo_tres.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.univalle_mobile_dogapp_equipo_tres.model.Appointment
import com.example.univalle_mobile_dogapp_equipo_tres.utils.Constants

@Database(entities = [Appointment::class], version = 1, exportSchema = false)
abstract class AppointmentDB : RoomDatabase() {

    abstract fun appointmentDao(): AppointmentDao

    companion object {
        // @Volatile makes writes visible to all threads immediately
        @Volatile
        private var INSTANCE: AppointmentDB? = null

        fun getDatabase(context: Context): AppointmentDB {
            // Return the existing instance, or create it once (thread-safe)
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppointmentDB::class.java,
                    Constants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}