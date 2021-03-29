package com.zvonimirplivelic.chuckfacts.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zvonimirplivelic.chuckfacts.repository.ChuckFactsRepository

class ChuckFactsViewModelFactory(
    val app: Application,
    private val chuckFactsRepository: ChuckFactsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChuckFactsViewModel(app, chuckFactsRepository) as T
    }
}