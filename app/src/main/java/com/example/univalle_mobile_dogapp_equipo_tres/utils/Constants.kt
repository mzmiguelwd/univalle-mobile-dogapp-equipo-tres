package com.example.univalle_mobile_dogapp_equipo_tres.utils

object Constants {
    // Local database
    const val DATABASE_NAME = "dogapp_database"
    const val TABLE_APPOINTMENTS = "appointments"

    // Remote API
    const val BASE_URL = "https://dog.ceo/api/"

    // Navigation argument keys
    const val ARG_APPOINTMENT_ID = "appointmentId"

    // The first item is the default "no selection" placeholder.
    val SYMPTOMS = listOf(
        "Síntomas",
        "Solo duerme",
        "No come",
        "Fractura extremidad",
        "Tiene pulgas",
        "Tiene garrapatas",
        "Bota demasiado pelo"
    )
}
