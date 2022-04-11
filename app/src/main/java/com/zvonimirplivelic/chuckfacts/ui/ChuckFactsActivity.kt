package com.zvonimirplivelic.chuckfacts.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.database.ChuckFactsDatabase
import com.zvonimirplivelic.chuckfacts.repository.ChuckFactsRepository
import com.zvonimirplivelic.chuckfacts.worker.PeriodicFactWork
import timber.log.Timber
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
class ChuckFactsActivity : AppCompatActivity() {
    lateinit var viewModel: ChuckFactsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chuckFactsRepository = ChuckFactsRepository(ChuckFactsDatabase.invoke(this))
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        val viewModelProviderFactory = ChuckFactsViewModelFactory(application, chuckFactsRepository)
        val randomFactFab: FloatingActionButton = findViewById(R.id.new_fact_fab)

        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(ChuckFactsViewModel::class.java)

//        setupPeriodicFactRequest()

        val navController = findNavController(R.id.chuck_facts_navigation_host_fragment)

        bottomNavigationView.apply {
            setupWithNavController(navController)
            background = null
            menu.getItem(1).isEnabled = false
        }

        randomFactFab.setOnClickListener {
            navController.navigate(R.id.navigation_single_fact)
        }

    }

//    private fun setupPeriodicFactRequest() {
//
//        val periodicFactRequest =
//            PeriodicWorkRequestBuilder<PeriodicFactWork>(24, TimeUnit.HOURS)
//                .build()
//
//        WorkManager.getInstance()
//            .enqueueUniquePeriodicWork(
//                "Periodic Fact Notification",
//                ExistingPeriodicWorkPolicy.REPLACE,
//                periodicFactRequest
//            )
//
//        Timber.d("Work request made")
//    }
}