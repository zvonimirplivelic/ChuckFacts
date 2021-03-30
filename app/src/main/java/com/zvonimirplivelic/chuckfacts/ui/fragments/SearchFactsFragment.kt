package com.zvonimirplivelic.chuckfacts.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.SyncStateContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.adapter.ChuckFactAdapter
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsActivity
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsViewModel
import com.zvonimirplivelic.chuckfacts.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchFactsFragment : Fragment(R.layout.search_facts_fragment) {

    private lateinit var viewModel: ChuckFactsViewModel
    lateinit var factAdapter: ChuckFactAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var etSearch: EditText
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ((activity) as ChuckFactsActivity).viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.search_facts_rv)
        setupRecyclerView()

        var job: Job ? = null
        etSearch = view.findViewById(R.id.search_facts_et)
        progressBar = view.findViewById(R.id.search_progress_bar)

        etSearch.addTextChangedListener{ editableString ->
            job?.cancel()
            job = MainScope().launch {
                delay(1000L)
                editableString?.let {
                    if(editableString.toString().isNotEmpty()) {
                        viewModel.getSearchedFact(editableString.toString())
                    } 
                }
            }
        }


        viewModel.searchFact.observe(viewLifecycleOwner, { response ->
            when (response) {

                is Resource.Success -> {
                    progressBar.visibility = View.INVISIBLE
                    response.data?.let { factResponse ->
                        factAdapter.differ.submitList(factResponse.factList)
                    }
                }

                is Resource.Error -> {
                    progressBar.visibility = View.INVISIBLE
                    response.message?.let { message ->
                        Timber.d("Errormessage ${message}")
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setupRecyclerView() {
        factAdapter = ChuckFactAdapter()
        recyclerView.apply {
            adapter = factAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}