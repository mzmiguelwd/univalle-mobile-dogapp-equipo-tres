package com.example.univalle_mobile_dogapp_equipo_tres.repository

import androidx.lifecycle.LiveData
import com.example.univalle_mobile_dogapp_equipo_tres.data.AppointmentDao
import com.example.univalle_mobile_dogapp_equipo_tres.model.Appointment

class AppointmentRepository(
    private val appointmentDao: AppointmentDao
) {

    val allAppointments: LiveData<List<Appointment>> = appointmentDao.getAllAppointments()

    suspend fun getAppointmentById(id: Int): Appointment? {
        return appointmentDao.getAppointmentById(id)
    }

    suspend fun insertAppointment(appointment: Appointment) {
        appointmentDao.insertAppointment(appointment)
    }

    suspend fun updateAppointment(appointment: Appointment) {
        appointmentDao.updateAppointment(appointment)
    }

    suspend fun deleteAppointment(appointment: Appointment) {
        appointmentDao.deleteAppointment(appointment)
    }
}
