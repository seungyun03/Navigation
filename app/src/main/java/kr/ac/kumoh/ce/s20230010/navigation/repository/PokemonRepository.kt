package kr.ac.kumoh.ce.s20230010.navigation.repository

import kr.ac.kumoh.ce.s20230010.navigation.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemonList() : List<Pokemon>
    suspend fun getPokemonById(id : Int) : Pokemon?
}