package kr.ac.kumoh.ce.s20230010.navigation.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.kumoh.ce.s20230010.navigation.repository.PokemonRepository

class PokemonDetailViewModel(
    private val repository: PokemonRepository,
    private val pokemonId : Int
): ViewModel()
{
    companion object{
        private val fallback = Pokemon(-1,"없음","없음","없음")
    }
    private val _pokemon = MutableStateFlow(fallback)
    val pokemon = _pokemon.asStateFlow()
    init {
        viewModelScope.launch{
            _pokemon.value = repository.getPokemonById(pokemonId)?: fallback
        }
    }

}