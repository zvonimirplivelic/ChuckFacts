package com.zvonimirplivelic.chuckfacts.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.os.Build
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
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

    val searchFact: MutableLiveData<Resource<ChuckFact>> = MutableLiveData()
    var searchFactResponse: ChuckFact? = null

    val randomFactList: MutableLiveData<List<ChuckFact>> = MutableLiveData()

    init {
        getRandomFact()
    }

    fun getRandomFact() = viewModelScope.launch {
        safeRandomFactCall()
    }

    fun getSearchedFact(searchString: String) = viewModelScope.launch {
        safeSearchFactsCall(searchString)
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

    private suspend fun safeSearchFactsCall(searchString: String) {
        searchFact.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {

                val response = chuckFactsRepository.searchForFact(searchString)
                searchFact.postValue(handleSearchFactResponse(response))
            } else {
                searchFact.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchFact.postValue(Resource.Error("Network Failure"))
                else -> searchFact.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleRandomFactResponse(response: Response<ChuckFact>): Resource<ChuckFact> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if (randomFactResponse == null) {
                    randomFactResponse == resultResponse
                    saveRandomFact(resultResponse)
                }

                return Resource.Success(randomFactResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }


    private fun handleSearchFactResponse(response: Response<ChuckFact>): Resource<ChuckFact> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if (searchFactResponse == null) {
                    searchFactResponse == resultResponse
                }

                return Resource.Success(searchFactResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    fun getSavedFacts() = chuckFactsRepository.getSavedFacts()

    private fun saveRandomFact(chuckFact: ChuckFact) = viewModelScope.launch {
        chuckFactsRepository.saveChuckFact(chuckFact)
    }

    fun deleteFact(chuckFact: ChuckFact) = viewModelScope.launch {
        chuckFactsRepository.deleteChuckFact(chuckFact)
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
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {

            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    Email.TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}