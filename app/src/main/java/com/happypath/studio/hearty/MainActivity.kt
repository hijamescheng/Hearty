package com.happypath.studio.hearty

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.happypath.studio.hearty.core.ui.AddDataScreenTopBar
import com.happypath.studio.hearty.core.ui.BottomNavigationBar
import com.happypath.studio.hearty.core.ui.Destination
import com.happypath.studio.hearty.core.ui.TopBar
import com.happypath.studio.hearty.core.ui.theme.DarkGreen
import com.happypath.studio.hearty.core.ui.theme.HeartyTheme
import com.happypath.studio.hearty.core.ui.theme.Pink40
import com.happypath.studio.hearty.feature.adddata.AddDataScreen
import com.happypath.studio.hearty.feature.adddata.AddDataViewModel
import com.happypath.studio.hearty.feature.home.HomePage
import com.happypath.studio.hearty.feature.home.HomeTabDestination
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

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
                            Destination.AddData.route -> {
                                val addDataEntry = remember(navController, Destination.AddData.route) {
                                    navController.getBackStackEntry(Destination.AddData.route)
                                }
                                val viewModel: AddDataViewModel = hiltViewModel(addDataEntry)
                                AddDataScreenTopBar(navController, viewModel)
                            }
                            else -> TopBar()
                        }

                    },
                    floatingActionButton = {
                        if (currentRoute != Destination.AddData.route) {
                            FloatingActionButton(
                                containerColor = Pink40,
                                contentColor = DarkGreen,
                                onClick = {
                                    navController.navigate(Destination.AddData.route)
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
            Destination.entries.forEach { destination ->
                composable(destination.route) {
                    when (destination) {
                        Destination.HOME -> HomePage(innerPadding)
                        Destination.Journal, Destination.Profile -> TestPage(innerPadding)
                        else -> {
                            val addDataEntry = remember(navController, Destination.AddData.route) {
                                navController.getBackStackEntry(Destination.AddData.route)
                            }
                            val addDataViewModel: AddDataViewModel = hiltViewModel(addDataEntry)

                            AddDataScreen(innerPadding, addDataViewModel)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TestPage(innerPadding: PaddingValues) {
        Text(
            text = "Test Page",
            modifier = Modifier.padding(innerPadding)
        )
    }
}