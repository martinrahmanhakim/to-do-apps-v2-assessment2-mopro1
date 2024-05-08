package com.d3if3058.assessment2.navigation

import com.d3if3058.assessment2.screen.KEY_ID_TASK

sealed class Screen(val route: String) {
    data object Welcome: Screen ("welcomeScreen")
    data object Home: Screen ("mainScreen")
    data object About: Screen ("aboutScreen")
    data object FormBaru: Screen ("detailScreen")
    data object FormUbah: Screen ("detailScreen/{$KEY_ID_TASK}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}