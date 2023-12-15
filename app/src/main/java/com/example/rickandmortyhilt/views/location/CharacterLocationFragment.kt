package com.example.rickandmortyhilt.views.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.rickandmortyhilt.R
import com.example.rickandmortyhilt.databinding.FragmentCharacterLocationBinding
import com.example.rickandmortyhilt.data.models.Locations
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterLocationFragment : Fragment() {
    private val viewModel: LocationsViewModel by viewModels()
    private var _binding: FragmentCharacterLocationBinding? = null
    private val binding: FragmentCharacterLocationBinding get() = _binding!!

    companion object {
        const val CharacterID = "CharacterID"
        const val CharacterImage = "CharacterImage"
        fun newInstance(bundle: Bundle) =
            CharacterLocationFragment().apply {
                arguments = bundle
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCharacterLocationBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        with(viewModel) {
            characterLocation.observe(viewLifecycleOwner) { showLocations(it) }
            errorMessage.observe(viewLifecycleOwner) { showError(it) }
            arguments?.getInt(CharacterID)?.let {
                getLocations(it)
            }
        }
    }

    private fun showLocations(locations: Locations?) {
        binding.locationsProgress.isVisible = true
        showCharacterImage()
        locations?.let {
            with(binding) {
                locationName.text = it.name
                type.text = it.type
                dimension.text = it.dimension
                numberOfResidents.text = it.residents?.size.toString()
            }
            it.name
        } ?: showError(getString(R.string.no_locations_available))
        binding.locationsProgress.isVisible = false
    }

    private fun showCharacterImage() {
        arguments?.getString(CharacterImage)?.let {
            Picasso.get().load(it).fit().into(binding.characterImage)
        }
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(
            requireContext(),
            errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}