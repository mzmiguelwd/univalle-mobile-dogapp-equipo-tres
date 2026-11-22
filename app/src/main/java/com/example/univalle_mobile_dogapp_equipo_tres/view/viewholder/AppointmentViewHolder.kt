package com.example.univalle_mobile_dogapp_equipo_tres.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.univalle_mobile_dogapp_equipo_tres.R
import com.example.univalle_mobile_dogapp_equipo_tres.databinding.ItemAppointmentBinding
import com.example.univalle_mobile_dogapp_equipo_tres.model.Appointment

class AppointmentViewHolder(
    private val binding: ItemAppointmentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(appointment: Appointment, onItemClick: (Appointment) -> Unit) {
        binding.appointment = appointment
        binding.executePendingBindings()

        binding.ivPetImage.load(appointment.imageUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
            placeholder(R.drawable.dog_head)
            error(R.drawable.dog_head)
        }

        binding.root.setOnClickListener {
            onItemClick(appointment)
        }
    }
}