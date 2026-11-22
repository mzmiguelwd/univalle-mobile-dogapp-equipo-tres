package com.example.univalle_mobile_dogapp_equipo_tres.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.univalle_mobile_dogapp_equipo_tres.data.AppointmentDB
import com.example.univalle_mobile_dogapp_equipo_tres.model.Appointment
import com.example.univalle_mobile_dogapp_equipo_tres.repository.AppointmentRepository
import kotlinx.coroutines.launch

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppointmentRepository

    val allAppointments: LiveData<List<Appointment>>

    private val _selectedAppointment = MutableLiveData<Appointment?>()
    val selectedAppointment: LiveData<Appointment?> = _selectedAppointment

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _saveSuccess = MutableLiveData(false)
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    private val _deleteSuccess = MutableLiveData(false)
    val deleteSuccess: LiveData<Boolean> = _deleteSuccess

    private val _updateSuccess = MutableLiveData(false)
    val updateSuccess: LiveData<Boolean> = _updateSuccess

    init {
        val dao = AppointmentDB.getDatabase(application).appointmentDao()
        repository = AppointmentRepository(dao)
        allAppointments = repository.allAppointments
    }

    fun createAppointment(
        petName: String,
        breed: String,
        ownerName: String,
        phone: String,
        symptom: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val appointment = Appointment(
                petName = petName.trim(),
                breed = breed.trim(),
                ownerName = ownerName.trim(),
                phone = phone.trim(),
                symptom = symptom,
                imageUrl = ""
            )
            repository.insertAppointment(appointment)
            _isLoading.value = false
            _saveSuccess.value = true
        }
    }

    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }

    fun loadAppointmentById(id: Int) {
        viewModelScope.launch {
            _selectedAppointment.value = repository.getAppointmentById(id)
        }
    }

    fun insertAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repository.insertAppointment(appointment)
        }
    }

    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repository.updateAppointment(appointment)
        }
    }

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repository.deleteAppointment(appointment)
            _selectedAppointment.value = null
            _deleteSuccess.value = true
        }
    }

    fun resetDeleteSuccess() {
        _deleteSuccess.value = false
    }

    fun editAppointment(
        original: Appointment,
        petName: String,
        breed: String,
        ownerName: String,
        phone: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val trimmedBreed = breed.trim()

            val updatedAppointment = original.copy(
                petName = petName.trim(),
                breed = trimmedBreed,
                ownerName = ownerName.trim(),
                phone = phone.trim()
            )
            repository.updateAppointment(updatedAppointment)
            _selectedAppointment.value = updatedAppointment
            _isLoading.value = false
            _updateSuccess.value = true
        }
    }

    fun resetUpdateSuccess() {
        _updateSuccess.value = false
    }
}
