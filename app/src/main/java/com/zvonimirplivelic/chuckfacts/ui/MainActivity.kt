package com.zvonimirplivelic.chuckfacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.zvonimirplivelic.chuckfacts.remote.RetrofitInstance
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var chuckFactTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var newFactBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chuckFactTextView = findViewById(R.id.chuck_fact_tv)
        progressBar = findViewById(R.id.progress_bar)
        newFactBtn = findViewById(R.id.new_fact_btn)

        randomFactNetworkCall()

        newFactBtn.setOnClickListener{
            chuckFactTextView.isVisible = false
            randomFactNetworkCall()
        }

    }

    private fun randomFactNetworkCall() {
        lifecycleScope.launchWhenCreated {
            progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getRandomFact()
            } catch (e: IOException) {
                Timber.d( "IOException, you might not have internet connection")
                progressBar.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Timber.d( "HTTPException, you might not have internet connection")
                progressBar.isVisible = false
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null) {
                chuckFactTextView.text = response.body()!!.fact
            } else {
                Timber.d("Response isn't successful")
            }

            progressBar.isVisible = false

            if(!chuckFactTextView.isVisible) {
                chuckFactTextView.isVisible = true
            }
        }
    }
}