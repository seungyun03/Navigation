package kr.ac.kumoh.ce.s20230010.navigation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.kumoh.ce.s20230010.navigation.repository.PokemonRepository

class PokemonListViewModel(
    private val repository: PokemonRepository
): ViewModel()
{
    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList = _pokemonList.asStateFlow()
    init{
        viewModelScope.launch {
            _pokemonList.value = repository.getPokemonList()
        }
    }
}

