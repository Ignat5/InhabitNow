package com.example.inhabitnow.android.navigation.root

import androidx.annotation.DrawableRes
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.navigation.AppNavDest

sealed class RootDestination(
    val destination: AppNavDest,
    val title: String,
    @DrawableRes val iconResId: Int
) {
    sealed class Main(
        destination: AppNavDest,
        title: String,
        @DrawableRes iconResId: Int
    ) : RootDestination(destination, title, iconResId) {
        data object ViewSchedule : Main(
            destination = AppNavDest.ViewScheduleDestination,
            title = "Today",
            iconResId = R.drawable.ic_today
        )

        data object ViewHabits : Main(
            destination = AppNavDest.ViewHabitsDestination,
            title = "Habits",
            iconResId = R.drawable.ic_habit
        )

        data object ViewTasks : Main(
            destination = AppNavDest.ViewTasksDestination,
            title = "Tasks",
            iconResId = R.drawable.ic_task
        )
    }

    sealed class Other(
        destination: AppNavDest,
        title: String,
        @DrawableRes iconResId: Int
    ) : RootDestination(destination, title, iconResId) {
        data object ViewSettings : Other(
            destination = AppNavDest.ViewSettingsDestination,
            title = "Settings",
            iconResId = R.drawable.ic_settings
        )
    }

    companion object {
        private val allDestinations by lazy {
            listOf(
                Main.ViewSchedule,
                Main.ViewHabits,
                Main.ViewTasks,
                Other.ViewSettings
            )
        }
        val allMainDestinations: List<Main> by lazy {
            allDestinations.filterIsInstance<Main>()
        }

        val allOtherDestinations: List<Other> by lazy {
            allDestinations.filterIsInstance<Other>()
        }

        val allMainDestinationsRoutes by lazy {
            allMainDestinations.map { it.destination.route }
        }
    }
}