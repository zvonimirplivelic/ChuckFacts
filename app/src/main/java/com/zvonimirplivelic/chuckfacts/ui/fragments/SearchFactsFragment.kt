package com.zvonimirplivelic.chuckfacts.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsActivity
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsViewModel

class SearchFactsFragment : Fragment() {

    private lateinit var viewModel: ChuckFactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ((activity) as ChuckFactsActivity).viewModel

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}