package com.example.univalle_mobile_dogapp_equipo_tres.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.univalle_mobile_dogapp_equipo_tres.utils.Constants

@Entity(tableName = Constants.TABLE_APPOINTMENTS)
data class Appointment(
    // Auto-generated ID, also used as the attention turn
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val petName: String,
    val breed: String,
    val ownerName: String,
    val phone: String,
    val symptom: String,
    // URL of the dog image obtained from the API, based on the breed
    val imageUrl: String
)