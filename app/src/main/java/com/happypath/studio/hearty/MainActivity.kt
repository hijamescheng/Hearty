package com.happypath.studio.hearty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.happypath.studio.hearty.ui.AddDataScreen
import com.happypath.studio.hearty.ui.AddDataScreenTopBar
import com.happypath.studio.hearty.ui.theme.HeartyTheme

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
                            BottomNavigationBar(navController)
                        }
                    },
                    topBar = {
                        when (currentRoute) {
                            Destination.AddData.route -> AddDataScreenTopBar(navController)
                            else -> TopBar()
                        }

                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate(Destination.AddData.route)
                            }
                        ) {
                            Icon(Icons.Default.Add, "Add measurement")
                        }
                    }
                ) { innerPadding ->
                    AppNavHost(innerPadding, navController, Destination.HOME)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(text = "Hearty")
        }
    )
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
                    Destination.Diary, Destination.Profile -> TestPage(innerPadding)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    innerPadding: PaddingValues
) {
    val navController = rememberNavController()
    val currentDestination by navController
        .currentBackStackEntryAsState()

    val selectedDestination = HomeTabDestination
        .entries
        .indexOfFirst {
            it.route == currentDestination?.destination?.route
        }
        .coerceAtLeast(0)

    Column {
        PrimaryTabRow(
            selectedTabIndex = selectedDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            HomeTabDestination.entries.forEachIndexed { index, destination ->
                Tab(
                    selected = selectedDestination == index,
                    onClick = {
                        navController.navigate(destination.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    },
                    text = {
                        Text(
                            text = destination.label,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
        HomeTabNavHost(innerPadding, navController)
    }
}

@Composable
fun HomeTabNavHost(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeTabDestination.Day.route
    ) {
        composable(HomeTabDestination.Day.route) {
            HomeTabScreen(innerPadding, HomeTabDestination.Day)
        }
        composable(HomeTabDestination.Week.route) {
            HomeTabScreen(innerPadding, HomeTabDestination.Week)
        }
        composable(HomeTabDestination.Month.route) {
            HomeTabScreen(innerPadding, HomeTabDestination.Month)
        }
        composable(HomeTabDestination.Year.route) {
            HomeTabScreen(innerPadding, HomeTabDestination.Year)
        }
    }
}

@Composable
fun HomeTabScreen(innerPadding: PaddingValues, destination: HomeTabDestination) {
    Text(destination.label, modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val startDestination = Destination.HOME
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
        Destination.entries.filter { it.isBottomNav }.forEachIndexed { index, destination ->
            NavigationBarItem(
                selected = selectedDestination == index,
                onClick = {
                    navController.navigate(destination.route)
                    selectedDestination = index
                },
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = destination.contentDescription
                    )
                },
                label = {
                    Text(text = destination.label)
                }
            )
        }
    }
}

enum class HomeTabDestination(
    val route: String,
    val label: String,
    val contentDescription: String
) {
    Day("day", "Day", "Day"),
    Week("week", "Week", "Week"),
    Month("month", "Month", "Month"),
    Year("year", "Year", "Year")
}

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String,
    val isBottomNav: Boolean = true
) {
    HOME("home", "Home", Icons.Default.Home, "Home"),
    Diary("diary", "Diary", Icons.Default.Menu, "Diary"),
    Profile("profile", "Profile", Icons.Default.Person, "Profile"),
    AddData("add_data", "Add data", Icons.Default.Add, "Add data", false)
}