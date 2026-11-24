package com.example.univalle_mobile_dogapp_equipo_tres.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.univalle_mobile_dogapp_equipo_tres.R
import com.example.univalle_mobile_dogapp_equipo_tres.databinding.FragmentAppointmentDetailBinding
import com.example.univalle_mobile_dogapp_equipo_tres.model.Appointment
import com.example.univalle_mobile_dogapp_equipo_tres.utils.Constants
import com.example.univalle_mobile_dogapp_equipo_tres.viewmodel.AppointmentViewModel

class AppointmentDetailFragment : Fragment() {

    private var _binding: FragmentAppointmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AppointmentViewModel

    private var appointmentId: Int = -1
    private var currentAppointment: Appointment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[AppointmentViewModel::class.java]
        appointmentId = arguments?.getInt(Constants.ARG_APPOINTMENT_ID, -1) ?: -1

        setupButtons()
        observeViewModel()

        viewModel.loadAppointmentById(appointmentId)
    }

    private fun setupButtons() {
        // Criterio 1: la flecha de atrás regresa al Home
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Criterio 4: elimina la cita de Room y regresa al Home
        binding.fabDelete.setOnClickListener {
            currentAppointment?.let { viewModel.deleteAppointment(it) }
        }

        // Criterio 5: navega a la HU 5.0 Editar Cita con el id de la cita
        binding.fabEdit.setOnClickListener {
            currentAppointment?.let { appointment ->
                val args = bundleOf(Constants.ARG_APPOINTMENT_ID to appointment.id)
                findNavController().navigate(
                    R.id.action_appointmentDetailFragment_to_editAppointmentFragment,
                    args
                )
            }
        }
    }

    private fun observeViewModel() {
        viewModel.selectedAppointment.observe(viewLifecycleOwner) { appointment ->
            // El ViewModel es compartido: se ignora una cita anterior que no sea la seleccionada
            if (appointment == null || appointment.id != appointmentId) return@observe
            showAppointment(appointment)
        }

        viewModel.deleteSuccess.observe(viewLifecycleOwner) { deleted ->
            if (deleted) {
                viewModel.resetDeleteSuccess()
                findNavController().popBackStack()
            }
        }
    }

    private fun showAppointment(appointment: Appointment) {
        currentAppointment = appointment
        binding.appointment = appointment
        binding.executePendingBindings()

        binding.ivPetImage.load(appointment.imageUrl) {
            crossfade(true)
            placeholder(R.drawable.dog_head)
            error(R.drawable.dog_head)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
