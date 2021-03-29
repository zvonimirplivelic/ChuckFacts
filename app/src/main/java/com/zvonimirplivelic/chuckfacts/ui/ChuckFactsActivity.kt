package com.zvonimirplivelic.chuckfacts.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.database.ChuckFactsDatabase
import com.zvonimirplivelic.chuckfacts.repository.ChuckFactsRepository

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewModel: ChuckFactsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chuckFactsRepository = ChuckFactsRepository(ChuckFactsDatabase.invoke(this))
        val viewModelProviderFactory = ChuckFactsViewModelFactory(application, chuckFactsRepository)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ChuckFactsViewModel::class.java)

        bottomNavigationView = findViewById(R.id.bottom_nav_view)

        val navController = findNavController(R.id.chuck_facts_navigation_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_new_fact, R.id.navigation_fact_list, R.id.navigation_fact_search))
        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false

    }
}