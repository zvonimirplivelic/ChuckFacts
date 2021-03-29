package com.zvonimirplivelic.chuckfacts.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.adapter.ChuckFactAdapter
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsActivity
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsViewModel

class FactListFragment : Fragment(R.layout.fact_list_fragment) {
    private lateinit var viewModel: ChuckFactsViewModel
    lateinit var chuckFactAdapter: ChuckFactAdapter
    lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.saved_facts_list_rv)
        viewModel = ((activity) as ChuckFactsActivity).viewModel


        setupRecyclerView()

        viewModel.getSavedFacts().observe(viewLifecycleOwner, Observer { chuckFacts ->
            chuckFactAdapter.differ.submitList(chuckFacts)
        })
    }

    private fun setupRecyclerView() {
        chuckFactAdapter = ChuckFactAdapter()
        recyclerView.apply {
            adapter = chuckFactAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}