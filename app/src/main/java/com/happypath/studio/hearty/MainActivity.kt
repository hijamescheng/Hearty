package com.happypath.studio.hearty

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.happypath.studio.hearty.core.ui.AddDataScreenTopBar
import com.happypath.studio.hearty.core.ui.BottomNavigationBar
import com.happypath.studio.hearty.core.ui.Destination
import com.happypath.studio.hearty.core.ui.EditProfileScreenTopBar
import com.happypath.studio.hearty.core.ui.ProfileScreenTopBar
import com.happypath.studio.hearty.core.ui.TopBar
import com.happypath.studio.hearty.core.ui.theme.DarkGreen
import com.happypath.studio.hearty.core.ui.theme.HeartyTheme
import com.happypath.studio.hearty.core.ui.theme.Pink40
import com.happypath.studio.hearty.feature.adddata.AddDataScreen
import com.happypath.studio.hearty.feature.adddata.AddDataViewModel
import com.happypath.studio.hearty.feature.home.HistoryPage
import com.happypath.studio.hearty.feature.home.HomePage
import com.happypath.studio.hearty.feature.profile.EditProfilePage
import com.happypath.studio.hearty.feature.profile.ProfileScreen
import com.happypath.studio.hearty.feature.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnrememberedGetBackStackEntry")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HeartyTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute in Destination.entries.filter { it.isBottomNav }
                                .map { it.route }) {
                            val currentRouteIndex = Destination.entries.indexOfFirst {
                                it.route == currentRoute
                            }
                            BottomNavigationBar(navController, currentRouteIndex)
                        }
                    },
                    topBar = {
                        when (currentRoute) {
                            Destination.ADD_DATA.route -> {
                                val addDataEntry =
                                    remember(navController, Destination.ADD_DATA.route) {
                                        navController.getBackStackEntry(Destination.ADD_DATA.route)
                                    }
                                val viewModel: AddDataViewModel = hiltViewModel(addDataEntry)
                                AddDataScreenTopBar(navController, viewModel)
                            }

                            Destination.PROFILE.route -> {
                                val profileEntry =
                                    remember(navController, Destination.PROFILE.route) {
                                        navController.getBackStackEntry(Destination.PROFILE.route)
                                    }
                                val profileViewModel: ProfileViewModel = hiltViewModel(profileEntry)

                                ProfileScreenTopBar(profileViewModel, {
                                    navController.navigate(Destination.EDIT_PROFILE.route)
                                })
                            }

                            Destination.EDIT_PROFILE.route -> {
                                val profileEntry =
                                    remember(navController, Destination.PROFILE.route) {
                                        navController.getBackStackEntry(Destination.PROFILE.route)
                                    }
                                val profileViewModel: ProfileViewModel = hiltViewModel(profileEntry)

                                EditProfileScreenTopBar(navController, {
                                    profileViewModel.onEvent(ProfileViewModel.ProfileEvent.Save)
                                    navController.navigate(
                                        Destination.PROFILE.route
                                    )
                                })
                            }

                            else -> TopBar()
                        }

                    },
                    floatingActionButton = {
                        if (currentRoute !in listOf(Destination.ADD_DATA.route, Destination.EDIT_PROFILE.route)) {
                            FloatingActionButton(
                                containerColor = Pink40,
                                contentColor = DarkGreen,
                                onClick = {
                                    navController.navigate(Destination.ADD_DATA.route)
                                }
                            ) {
                                Icon(Icons.Default.Add, "Add measurement")
                            }
                        }
                    }
                ) { innerPadding ->
                    AppNavHost(innerPadding, navController, Destination.HOME)
                }
            }
        }
    }

    @SuppressLint("UnrememberedGetBackStackEntry")
    @Composable
    fun AppNavHost(
        innerPadding: PaddingValues,
        navController: NavHostController,
        startDestination: Destination
    ) {
        NavHost(
            navController,
            startDestination = startDestination.route
        ) {
            composable(Destination.HOME.route) {
                HomePage(innerPadding)
            }

            composable(Destination.JOURNAL.route) {
                HistoryPage(innerPadding)
            }

            navigation(
                startDestination = Destination.PROFILE.route,
                route = "profile_flow"
            ) {
                composable(Destination.PROFILE.route) { backStackEntry ->
                    val profileViewModel: ProfileViewModel =
                        getSharedViewModel(navController, backStackEntry, "profile_flow")
                    ProfileScreen(innerPadding, profileViewModel)
                }

                composable(Destination.EDIT_PROFILE.route) { backStackEntry ->
                    val profileViewModel: ProfileViewModel =
                        getSharedViewModel(navController, backStackEntry, "profile_flow")
                    EditProfilePage(innerPadding, profileViewModel)
                }
            }

            composable(Destination.ADD_DATA.route) {
                val addDataEntry = remember(navController, Destination.ADD_DATA.route) {
                    navController.getBackStackEntry(Destination.ADD_DATA.route)
                }
                val addDataViewModel: AddDataViewModel = hiltViewModel(addDataEntry)

                AddDataScreen(innerPadding, addDataViewModel)
            }
        }
    }

    @Composable
    inline fun <reified T : ViewModel> getSharedViewModel(
        navController: NavController,
        currentEntry: NavBackStackEntry,
        parentRoute: String
    ): T {
        // 1. Find the parent graph's entry in the backstack
        val parentEntry = remember(currentEntry) {
            navController.getBackStackEntry(parentRoute)
        }
        // 2. Scope the Hilt ViewModel to that parent entry
        return hiltViewModel(parentEntry)
    }
}