package com.example.githubapi.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.githubapi.R

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun openSearch(){
        val searchMenuItem = onView(withId(R.id.search))
        searchMenuItem.perform(click())

        onView(withHint(R.string.search_hint))
            .perform(typeText("benhard"))

        onView(withHint(R.string.search_hint))
            .perform(pressImeActionButton())
    }
}