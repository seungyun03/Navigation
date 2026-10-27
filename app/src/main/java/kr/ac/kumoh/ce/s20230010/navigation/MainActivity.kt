package kr.ac.kumoh.ce.s20230010.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import kr.ac.kumoh.ce.s20230010.navigation.model.PokemonDetailViewModel
import kr.ac.kumoh.ce.s20230010.navigation.model.PokemonListViewModel
import kr.ac.kumoh.ce.s20230010.navigation.repository.MockPockemonRepository
import kr.ac.kumoh.ce.s20230010.navigation.repository.PokemonRepository
import kr.ac.kumoh.ce.s20230010.navigation.ui.theme.NavigationTheme
import kotlin.collections.listOf

sealed interface pokemonRoute : NavKey
{
    @Serializable
    data object pokemonList : pokemonRoute
    @Serializable
    data class pokemonDetail(val id:Int) : pokemonRoute
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository: PokemonRepository = MockPockemonRepository()
        setContent {
            NavigationTheme {
              MainScreen(repository)
            }
        }
    }
}

@Composable
fun MainScreen(
    repository: PokemonRepository
)
{
    val backStack = rememberNavBackStack(pokemonRoute.pokemonList)
    Scaffold(modifier = Modifier.fillMaxSize())
    {
        innerPadding->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<pokemonRoute.pokemonList>{
                 PokemonListScreen(repository,{id->backStack.add(pokemonRoute.pokemonDetail(id))})
                }
                entry<pokemonRoute.pokemonDetail>
                {
                    key->
                   PokemonDetailScreen(key.id,repository)
                }
            }
        )
    }
}
@Composable
fun PokemonListScreen(
    //pokemonList: List<Pokemon>,
    repository: PokemonRepository,
    onNavigateToDetail: (Int) -> Unit,
    viewModel: PokemonListViewModel = viewModel{PokemonListViewModel(repository)}
)
{
    val pokemonList by viewModel.pokemonList.collectAsStateWithLifecycle()
    LazyColumn{
        items(pokemonList){
                pokemon->
            Button(
                onClick = {
                    onNavigateToDetail(pokemon.id)
                }
            ) {
                Text(pokemon.name)
            }
        }
    }
}
@Composable
fun PokemonDetailScreen(
    id : Int,
    repository: PokemonRepository,
    viewModel: PokemonDetailViewModel = viewModel{
        PokemonDetailViewModel(repository,id)
    }
)
{
//    val pokemon = viewModel.getPokemonById(id)
//    pokemon?.let {
//        Text(it.Info)
//    }
    val pokemon by viewModel.pokemon.collectAsStateWithLifecycle()
    Text(pokemon.name)
}
