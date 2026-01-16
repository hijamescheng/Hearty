package com.happypath.studio.hearty

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
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.happypath.studio.hearty.ui.AddDataScreen
import com.happypath.studio.hearty.ui.AddDataScreenTopBar
import com.happypath.studio.hearty.ui.BottomNavigationBar
import com.happypath.studio.hearty.ui.Destination
import com.happypath.studio.hearty.ui.HomePage
import com.happypath.studio.hearty.ui.HomeTabDestination
import com.happypath.studio.hearty.ui.TopBar
import com.happypath.studio.hearty.ui.theme.DarkGreen
import com.happypath.studio.hearty.ui.theme.HeartyTheme
import com.happypath.studio.hearty.ui.theme.Pink40

class MainActivity : ComponentActivity() {
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
                            Destination.AddData.route -> AddDataScreenTopBar(navController)
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
}

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
                    else -> AddDataScreen(innerPadding)
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

@Composable
fun HomeTabScreen(innerPadding: PaddingValues, destination: HomeTabDestination) {
    Text(destination.label, modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
}