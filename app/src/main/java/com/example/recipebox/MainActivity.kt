package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.remote.RetrofitClient
import com.example.data.repository.RecipeRepository
import com.example.data.repository.ShoppingRepository
import com.example.ui.components.NavDestination
import com.example.ui.components.RecipeBoxBottomBar
import com.example.ui.screens.detail.RecipeDetailScreen
import com.example.ui.screens.explore.ExploreScreen
import com.example.ui.screens.favorites.FavoritesScreen
import com.example.ui.screens.shopping.ShoppingListScreen
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.RecipeBoxTheme
import com.example.ui.viewmodel.DetailViewModel
import com.example.ui.viewmodel.ExploreViewModel
import com.example.ui.viewmodel.FavoritesViewModel
import com.example.ui.viewmodel.ShoppingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val recipeRepository = RecipeRepository(
            apiService = RetrofitClient.recipeApi,
            favoriteDao = database.favoriteDao()
        )
        val shoppingRepository = ShoppingRepository(
            shoppingDao = database.shoppingDao()
        )

        setContent {
            RecipeBoxTheme {
                RecipeBoxApp(
                    recipeRepository = recipeRepository,
                    shoppingRepository = shoppingRepository
                )
            }
        }
    }
}

@Composable
fun RecipeBoxApp(
    recipeRepository: RecipeRepository,
    shoppingRepository: ShoppingRepository
) {
    var currentDestination by remember { mutableStateOf(NavDestination.EXPLORE) }
    var selectedRecipeId by remember { mutableStateOf<Int?>(null) }

    val exploreViewModel: ExploreViewModel = viewModel(
        factory = ExploreViewModel.provideFactory(recipeRepository)
    )
    val favoritesViewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModel.provideFactory(recipeRepository)
    )
    val detailViewModel: DetailViewModel = viewModel(
        factory = DetailViewModel.provideFactory(recipeRepository, shoppingRepository)
    )
    val shoppingViewModel: ShoppingViewModel = viewModel(
        factory = ShoppingViewModel.provideFactory(shoppingRepository)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp) // Support tablets and wide screens comfortably
        ) {
            val activeRecipeId = selectedRecipeId
            if (activeRecipeId != null) {
                // Detail Screen view
                RecipeDetailScreen(
                    recipeId = activeRecipeId,
                    viewModel = detailViewModel,
                    onBack = { selectedRecipeId = null }
                )
            } else {
                // Main Navigation Tabs
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = CreamBackground,
                    bottomBar = {
                        RecipeBoxBottomBar(
                            currentDestination = currentDestination,
                            onNavigateTo = { currentDestination = it }
                        )
                    }
                ) { innerPadding ->
                    AnimatedContent(
                        targetState = currentDestination,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "tab_navigation",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) { destination ->
                        when (destination) {
                            NavDestination.EXPLORE -> {
                                ExploreScreen(
                                    viewModel = exploreViewModel,
                                    onRecipeClick = { id -> selectedRecipeId = id }
                                )
                            }
                            NavDestination.FAVORITES -> {
                                FavoritesScreen(
                                    viewModel = favoritesViewModel,
                                    onRecipeClick = { id -> selectedRecipeId = id },
                                    onExploreClick = { currentDestination = NavDestination.EXPLORE }
                                )
                            }
                            NavDestination.SHOPPING -> {
                                ShoppingListScreen(
                                    viewModel = shoppingViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
