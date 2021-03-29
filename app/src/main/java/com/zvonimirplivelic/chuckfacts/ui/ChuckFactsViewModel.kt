package com.zvonimirplivelic.chuckfacts.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zvonimirplivelic.chuckfacts.ChuckFactsApplication
import com.zvonimirplivelic.chuckfacts.model.ChuckFact
import com.zvonimirplivelic.chuckfacts.repository.ChuckFactsRepository
import com.zvonimirplivelic.chuckfacts.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ChuckFactsViewModel(
    val app: Application,
    private val chuckFactsRepository: ChuckFactsRepository
) : AndroidViewModel(app) {

    val randomFact: MutableLiveData<Resource<ChuckFact>> = MutableLiveData()
    var randomFactResponse: ChuckFact? = null

    init {
        getRandomFact()
    }

    fun getRandomFact() = viewModelScope.launch {
        safeRandomFactCall()
    }

    private suspend fun safeRandomFactCall() {
        randomFact.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = chuckFactsRepository.getRandomChuckFact()
                delay(444L)
                randomFact.postValue(handleRandomFactResponse(response))
            } else {
                randomFact.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> randomFact.postValue(Resource.Error("Network Failure"))
                else -> randomFact.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleRandomFactResponse(response: Response<ChuckFact>): Resource<ChuckFact> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if (randomFactResponse == null) {
                    randomFactResponse == resultResponse
                }

                return Resource.Success(randomFactResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<ChuckFactsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}