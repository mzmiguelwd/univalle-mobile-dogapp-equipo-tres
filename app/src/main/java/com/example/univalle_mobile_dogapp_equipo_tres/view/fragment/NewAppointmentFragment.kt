package com.example.univalle_mobile_dogapp_equipo_tres.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.univalle_mobile_dogapp_equipo_tres.R
import com.example.univalle_mobile_dogapp_equipo_tres.databinding.FragmentNewAppointmentBinding
import com.example.univalle_mobile_dogapp_equipo_tres.utils.Constants
import com.example.univalle_mobile_dogapp_equipo_tres.viewmodel.AppointmentViewModel

class NewAppointmentFragment : Fragment() {

    private var _binding: FragmentNewAppointmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AppointmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[AppointmentViewModel::class.java]

        setupSymptomsSpinner()
        setupInputListeners()
        setupButtons()
        observeViewModel()

        viewModel.loadBreeds()
    }

    private fun setupSymptomsSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Constants.SYMPTOMS
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSymptoms.adapter = adapter
    }

    private fun setupInputListeners() {
        val textWatcher = { updateSaveButtonState() }
        binding.etPetName.doAfterTextChanged { textWatcher() }
        binding.actvBreed.doAfterTextChanged { textWatcher() }
        binding.etOwnerName.doAfterTextChanged { textWatcher() }
        binding.etPhone.doAfterTextChanged { textWatcher() }

        updateSaveButtonState()
    }

    private fun setupButtons() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSaveAppointment.setOnClickListener {
            if (viewModel.isLoading.value == true) return@setOnClickListener

            if (binding.spSymptoms.selectedItemPosition == 0) {
                Toast.makeText(
                    requireContext(),
                    "Selecciona un síntoma",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            viewModel.createAppointment(
                petName = binding.etPetName.text.toString(),
                breed = binding.actvBreed.text.toString(),
                ownerName = binding.etOwnerName.text.toString(),
                phone = binding.etPhone.text.toString(),
                symptom = binding.spSymptoms.selectedItem.toString()
            )
        }
    }

    private fun observeViewModel() {
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
            updateSaveButtonState()
        }

        viewModel.saveSuccess.observe(viewLifecycleOwner) { saved ->
            if (saved) {
                viewModel.resetSaveSuccess()
                findNavController().popBackStack()
            }
        }
    }

    private fun updateSaveButtonState() {
        val allFieldsFilled =
            !binding.etPetName.text.isNullOrBlank() &&
                    !binding.actvBreed.text.isNullOrBlank() &&
                    !binding.etOwnerName.text.isNullOrBlank() &&
                    !binding.etPhone.text.isNullOrBlank()

        val isLoading = viewModel.isLoading.value == true

        binding.btnSaveAppointment.isEnabled = allFieldsFilled && !isLoading

        val colorRes = if (allFieldsFilled) R.color.white else R.color.dark_gray
        val textStyle = if (allFieldsFilled) Typeface.BOLD else Typeface.NORMAL

        binding.btnSaveAppointment.setTextColor(
            ContextCompat.getColor(requireContext(), colorRes)
        )
        binding.btnSaveAppointment.setTypeface(null, textStyle)
        binding.btnSaveAppointment.iconTint =
            ContextCompat.getColorStateList(requireContext(), colorRes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
