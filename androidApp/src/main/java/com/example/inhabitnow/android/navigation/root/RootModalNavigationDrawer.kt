package com.example.inhabitnow.android.navigation.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.inhabitnow.android.R

@Composable
fun RootModalNavigationDrawer(
    currentBackStackEntry: NavBackStackEntry?,
    drawerState: DrawerState,
    onRootDestinationClick: (RootDestination) -> Unit,
    content: @Composable () -> Unit
) {
    val allMainDestinationRoutes = remember {
        RootDestination.allMainDestinationsRoutes
    }
    val gesturesEnabled = remember(currentBackStackEntry) {
        currentBackStackEntry?.destination?.route in allMainDestinationRoutes
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.padding(end = 64.dp)
            ) {
                val allMainDestinations = remember {
                    RootDestination.allMainDestinations
                }
                val allOtherDestinations = remember {
                    RootDestination.allOtherDestinations
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "InhabitNow",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    ItemSetNavigationDestination(
                        items = allMainDestinations,
                        currentBackStackEntry = currentBackStackEntry,
                        onClick = onRootDestinationClick
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    ItemSetNavigationDestination(
                        items = allOtherDestinations,
                        currentBackStackEntry = currentBackStackEntry,
                        onClick = onRootDestinationClick
                    )
                }
            }
        },
        content = content
    )
}

@Composable
private fun ItemSetNavigationDestination(
    items: List<RootDestination>,
    currentBackStackEntry: NavBackStackEntry?,
    onClick: (RootDestination) -> Unit
) {
    items.forEach { rootDestination ->
        val isSelected = remember(currentBackStackEntry) {
            currentBackStackEntry?.destination?.hierarchy?.any { it.route == rootDestination.destination.route } == true
        }
        ItemNavigationDestination(
            rootDestination = rootDestination,
            isSelected = isSelected,
            onClick = {
                onClick(rootDestination)
            }
        )
    }
}

@Composable
private fun ItemNavigationDestination(
    rootDestination: RootDestination,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(text = rootDestination.title)
        },
        icon = {
            Icon(
                painter = painterResource(id = rootDestination.iconResId),
                contentDescription = null
            )
        },
        selected = isSelected,
        onClick = onClick
    )
}