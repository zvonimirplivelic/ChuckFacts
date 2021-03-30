package com.zvonimirplivelic.chuckfacts.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.adapter.ChuckFactAdapter
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsActivity
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsViewModel
import kotlinx.coroutines.launch

class FactListFragment : Fragment(R.layout.fact_list_fragment) {
    private lateinit var viewModel: ChuckFactsViewModel
    lateinit var chuckFactAdapter: ChuckFactAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var clearListFab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ((activity) as ChuckFactsActivity).viewModel

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clearListFab: FloatingActionButton = view.findViewById(R.id.delete_all_facts_fab)

        recyclerView = view.findViewById(R.id.saved_facts_list_rv)
        setupRecyclerView()

        viewModel.getSavedFacts().observe(viewLifecycleOwner, { chuckFacts ->
            chuckFactAdapter.differ.submitList(chuckFacts)
        })

        clearListFab.setOnClickListener {
            lifecycleScope.launch { clearAllFacts()}
        }
    }

    private fun setupRecyclerView() {
        chuckFactAdapter = ChuckFactAdapter()
        recyclerView.apply {
            adapter = chuckFactAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private suspend fun clearAllFacts() {
        viewModel.deleteAllFacts()
    }
}