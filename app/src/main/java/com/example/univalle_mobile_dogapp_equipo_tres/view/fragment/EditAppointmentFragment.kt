package com.example.univalle_mobile_dogapp_equipo_tres.view.fragment


import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.univalle_mobile_dogapp_equipo_tres.R
import com.example.univalle_mobile_dogapp_equipo_tres.databinding.FragmentEditAppointmentBinding
import com.example.univalle_mobile_dogapp_equipo_tres.viewmodel.AppointmentViewModel
import com.example.univalle_mobile_dogapp_equipo_tres.model.Appointment
import com.example.univalle_mobile_dogapp_equipo_tres.utils.Constants

class EditAppointmentFragment : Fragment() {

    private var _binding: FragmentEditAppointmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AppointmentViewModel
    private var appointmentId: Int = -1
    private var currentAppointment: Appointment? = null
    private var isFormPopulated: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[AppointmentViewModel::class.java]
        appointmentId = arguments?.getInt(Constants.ARG_APPOINTMENT_ID, -1) ?: -1

        setupInputListeners()
        setupButtons()
        observeViewModel()

        viewModel.loadBreeds()
        if (appointmentId != -1) {
            viewModel.loadAppointmentById(appointmentId)
        }
    }

    private fun setupInputListeners() {
        val textWatcher = { updateEditButtonState() }
        binding.etPetName.doAfterTextChanged { textWatcher() }
        binding.actvBreed.doAfterTextChanged { textWatcher() }
        binding.etOwnerName.doAfterTextChanged { textWatcher() }
        binding.etPhone.doAfterTextChanged { textWatcher() }

        updateEditButtonState()
    }

    private fun setupButtons() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnEditAppointment.setOnClickListener {
            if (viewModel.isLoading.value == true) return@setOnClickListener
            val original = currentAppointment ?: return@setOnClickListener

            viewModel.editAppointment(
                original = original,
                petName = binding.etPetName.text.toString(),
                breed = binding.actvBreed.text.toString(),
                ownerName = binding.etOwnerName.text.toString(),
                phone = binding.etPhone.text.toString()
            )
        }
    }

    private fun observeViewModel() {
        viewModel.selectedAppointment.observe(viewLifecycleOwner) { appointment ->
            if (appointment != null && appointment.id == appointmentId && !isFormPopulated) {
                currentAppointment = appointment
                isFormPopulated = true

                binding.etPetName.setText(appointment.petName)
                // El segundo parámetro en false evita que el desplegable se abra automáticamente al precargar
                binding.actvBreed.setText(appointment.breed, false)
                binding.etOwnerName.setText(appointment.ownerName)
                binding.etPhone.setText(appointment.phone)

                updateEditButtonState()
            }
        }

        viewModel.breeds.observe(viewLifecycleOwner) { breeds ->
            val breedAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                breeds
            )
            binding.actvBreed.setAdapter(breedAdapter)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            updateEditButtonState()
        }

        viewModel.updateSuccess.observe(viewLifecycleOwner) { updated ->
            if (updated) {
                viewModel.resetUpdateSuccess()
                findNavController().navigate(R.id.action_editAppointmentFragment_to_homeFragment)
            }
        }
    }

    private fun updateEditButtonState() {
        val allFieldsFilled =
            !binding.etPetName.text.isNullOrBlank() &&
                    !binding.actvBreed.text.isNullOrBlank() &&
                    !binding.etOwnerName.text.isNullOrBlank() &&
                    !binding.etPhone.text.isNullOrBlank()

        val isLoading = viewModel.isLoading.value == true

        binding.btnEditAppointment.isEnabled = allFieldsFilled && !isLoading

        val colorRes = if (allFieldsFilled) R.color.white else R.color.dark_gray
        val textStyle = if (allFieldsFilled) Typeface.BOLD else Typeface.NORMAL

        binding.btnEditAppointment.setTextColor(
            ContextCompat.getColor(requireContext(), colorRes)
        )
        binding.btnEditAppointment.setTypeface(null, textStyle)
        binding.btnEditAppointment.iconTint =
            ContextCompat.getColorStateList(requireContext(), colorRes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}