package com.example.univalle_mobile_dogapp_equipo_tres.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.lifecycle.LiveData
import com.example.univalle_mobile_dogapp_equipo_tres.model.Appointment
import com.example.univalle_mobile_dogapp_equipo_tres.utils.Constants

@Dao
interface AppointmentDao {

    // Returns all appointments ordered by turn. LiveData keeps the UI updated automatically.
    @Query("SELECT * FROM ${Constants.TABLE_APPOINTMENTS} ORDER BY id ASC")
    fun getAllAppointments(): LiveData<List<Appointment>>

    // Returns a single appointment by its ID (null if it doesn't exist)
    @Query("SELECT * FROM ${Constants.TABLE_APPOINTMENTS} WHERE id = :id")
    suspend fun getAppointmentById(id: Int): Appointment?

    @Insert
    suspend fun insertAppointment(appointment: Appointment)

    @Update
    suspend fun updateAppointment(appointment: Appointment)

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)
}