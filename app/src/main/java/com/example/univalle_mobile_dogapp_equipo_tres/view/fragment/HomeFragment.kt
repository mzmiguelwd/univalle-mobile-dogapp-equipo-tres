package com.example.univalle_mobile_dogapp_equipo_tres.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.univalle_mobile_dogapp_equipo_tres.R
import com.example.univalle_mobile_dogapp_equipo_tres.databinding.FragmentHomeBinding
import com.example.univalle_mobile_dogapp_equipo_tres.utils.Constants
import com.example.univalle_mobile_dogapp_equipo_tres.view.adapter.AppointmentAdapter
import com.example.univalle_mobile_dogapp_equipo_tres.viewmodel.AppointmentViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AppointmentViewModel
    private lateinit var adapter: AppointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[AppointmentViewModel::class.java]

        setupRecyclerView()
        setupListeners()
        observeAppointments()
    }

    private fun setupRecyclerView() {
        adapter = AppointmentAdapter { appointment ->
            val args = bundleOf(Constants.ARG_APPOINTMENT_ID to appointment.id)
            findNavController().navigate(
                R.id.action_homeFragment_to_appointmentDetailFragment,
                args
            )
        }
        binding.rvAppointments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAppointments.adapter = adapter
    }

    private fun setupListeners() {
        binding.fabAddAppointment.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newAppointmentFragment)
        }
    }

    private fun observeAppointments() {
        viewModel.allAppointments.observe(viewLifecycleOwner) { appointments ->
            adapter.submitList(appointments)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
