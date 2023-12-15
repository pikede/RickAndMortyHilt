package com.example.rickandmortyhilt.views.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyhilt.databinding.CharacterDetailsBinding
import com.example.rickandmortyhilt.data.models.Result

class CharacterAdapter(private val listener: CharacterSelected) :
    PagingDataAdapter<Result, CharacterAdapter.CharacterViewHolder>(ResultComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CharacterDetailsBinding.inflate(layoutInflater, parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position)
        character?.let {
            holder.bind(it, listener)
        }
    }

    inner class CharacterViewHolder(private val binding: CharacterDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            character: Result,
            listener: CharacterSelected,
        ) {
            binding.characterName.text = character.name
            binding.characterSpecie.text = character.species
            binding.characterStatus.text = character.status
            binding.root.setOnClickListener {
                listener.showCharacterLocation(character)
            }
        }
    }
}

class ResultComparator : DiffUtil.ItemCallback<Result>() {
    override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem.id == newItem.id
    }
}

interface CharacterSelected {
    fun showCharacterLocation(character: Result)
}