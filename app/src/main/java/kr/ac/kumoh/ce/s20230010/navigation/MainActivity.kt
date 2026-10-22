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
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kr.ac.kumoh.ce.s20230010.navigation.ui.theme.NavigationTheme
import kotlin.collections.listOf

sealed interface pokemonRoute : NavKey
{
    @Serializable
    data object pokemonList : pokemonRoute
    @Serializable
    data class pokemonDetail(val id:Int) : pokemonRoute
}
data class Pokemon(val id:Int,val name: String, val Type:String, val Info : String)

val pokemons = listOf(
    Pokemon(1, "이상해씨", "풀/독", "태어날 때부터 등에 식물의 씨앗이 있어 조금씩 자란다."),
    Pokemon(4, "파이리", "불꽃", "태어날 때부터 꼬리에 불이 붙어 있다. 꺼지면 죽는다."),
    Pokemon(7, "꼬부기", "물", "등껍질이 단단하여 공격을 잘 막아내고 물을 뿜어낸다."),
    Pokemon(25, "피카츄", "전기", "뺨에 전기 주머니가 있어서 위기에 처하면 전기를 방출한다.")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationTheme {
              MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen()
{
    val backStack = rememberNavBackStack(pokemonRoute.pokemonList)
    Scaffold(modifier = Modifier.fillMaxSize())
    {
        innerPadding->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<pokemonRoute.pokemonList>{
                 pokemonListScreen(pokemons,{id->backStack.add(pokemonRoute.pokemonDetail(id))})
                }
                entry<pokemonRoute.pokemonDetail>
                {
                    key->
                   pokemonDetailScreen(key.id)
                }
            }
        )
    }
}
@Composable
fun pokemonListScreen(
    pokemonList: List<Pokemon>,
    onNavigateToDetail: (Int) -> Unit
)
{
    LazyColumn{
        items(pokemons){
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
fun pokemonDetailScreen(
    id : Int
)
{
    pokemons.find { it.id == id }?.let{
        Text(it.Info)
    }
}