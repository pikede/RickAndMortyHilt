package com.example.rickandmortyhilt.views.characters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.rickandmortyhilt.data.repository.CharactersPagingSource
import com.example.rickandmortyhilt.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    private var _characterName = MutableLiveData<String>()

    companion object {
        const val MAX_CHARACTERS_PER_PAGE = 20
    }

    init {
        getCharacters("")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val flow = _characterName.asFlow().flatMapLatest {
        Pager(PagingConfig(MAX_CHARACTERS_PER_PAGE)) {
            CharactersPagingSource(repository, it)
        }.flow.cachedIn(viewModelScope)
    }

    fun getCharacters(newCharacter: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (newCharacter.trim() != _characterName.value) {
                _characterName.postValue(newCharacter)
            }
        }
    }
}