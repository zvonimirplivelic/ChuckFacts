package com.zvonimirplivelic.chuckfacts.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.database.ChuckFactsDatabase
import com.zvonimirplivelic.chuckfacts.repository.ChuckFactsRepository
import com.zvonimirplivelic.chuckfacts.util.Constants
import com.zvonimirplivelic.chuckfacts.util.Resource
import com.zvonimirplivelic.chuckfacts.worker.PeriodicFactWork
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ChuckFactsActivity : AppCompatActivity() {
    lateinit var viewModel: ChuckFactsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        val chuckFactsRepository = ChuckFactsRepository(ChuckFactsDatabase.invoke(this))
        val viewModelProviderFactory = ChuckFactsViewModelFactory(application, chuckFactsRepository)
        val randomFactFab: FloatingActionButton = findViewById(R.id.new_fact_fab)

        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(ChuckFactsViewModel::class.java)



        setupPeriodicFactRequest()


        val navController = findNavController(R.id.chuck_facts_navigation_host_fragment)

        bottomNavigationView.apply {
            setupWithNavController(navController)
            background = null
            menu.getItem(1).isEnabled = false
        }

        randomFactFab.setOnClickListener {
            navController.navigate(R.id.navigation_new_fact)
        }

    }

    private fun setupPeriodicFactRequest() {
        var randomFactNotificationString: String? = null
        viewModel.getRandomFact()
        viewModel.randomFact.observe(this, { response ->
            when (response) {

                is Resource.Success -> {
                    response.data?.let { factResponse ->
                        randomFactNotificationString = factResponse.value
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(this, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })

        val inputData =
            workDataOf(Constants.RANDOM_FACT_NOTIFICATION_DATA to randomFactNotificationString)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val periodicFactRequest =
            PeriodicWorkRequestBuilder<PeriodicFactWork>(1, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()

        WorkManager.getInstance()
            .enqueue(periodicFactRequest)


        Timber.d("Work Request made")
    }
}