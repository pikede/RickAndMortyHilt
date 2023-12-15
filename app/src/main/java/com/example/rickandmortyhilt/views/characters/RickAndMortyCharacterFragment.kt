package com.example.rickandmortyhilt.views.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyhilt.data.models.Result
import com.example.rickandmortyhilt.databinding.FragmentRickAndMortyCharacterBinding
import com.example.rickandmortyhilt.views.location.CharacterLocationFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RickAndMortyCharacterFragment : Fragment(), CharacterSelected, OnQueryTextListener {
    private val viewModel: CharacterViewModel by viewModels()
    private var _binding: FragmentRickAndMortyCharacterBinding? = null
    private val binding: FragmentRickAndMortyCharacterBinding get() = _binding!!
    private lateinit var characterAdapter: CharacterAdapter

    companion object {
        fun newInstance() = RickAndMortyCharacterFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRickAndMortyCharacterBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupUI()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flow.collectLatest { pagedData ->
                    submitCharacters(pagedData)
                }
            }
        }
    }

    private suspend fun submitCharacters(characters: PagingData<Result>) {
        characterAdapter.submitData(characters)
    }

    private fun setupUI() {
        binding.characters.run {
            layoutManager = LinearLayoutManager(requireContext())
            characterAdapter = CharacterAdapter(this@RickAndMortyCharacterFragment)
            adapter = characterAdapter
        }
        binding.characterSearch.setOnQueryTextListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showCharacterLocation(character: Result) {
        activity?.supportFragmentManager?.beginTransaction()?.replace(
            android.R.id.content, CharacterLocationFragment.newInstance(
                bundleOf(
                    CharacterLocationFragment.CharacterImage to character.image,
                    CharacterLocationFragment.CharacterID to character.id
                )
            )
        )?.addToBackStack(null)?.commit()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            viewModel.getCharacters(query)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // this loads more characters when the text field gets empty
        if (newText.isNullOrEmpty() || newText.isBlank()) {
            viewModel.getCharacters("")
        }
        return false
    }
}