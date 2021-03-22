package com.zvonimirplivelic.chuckfacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.zvonimirplivelic.chuckfacts.remote.RetrofitInstance
import retrofit2.HttpException
import retrofit2.Retrofit
import timber.log.Timber
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chuckFactTextView: TextView = findViewById(R.id.chuck_fact_tv)
        val progressBar: ProgressBar = findViewById(R.id.progress_bar)

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
                chuckFactTextView.text = response.body()!!.value
            } else {
                Timber.d("Response isn't succesfull")
            }
            progressBar.isVisible = false
        }
    }
}