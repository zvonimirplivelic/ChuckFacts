package com.zvonimirplivelic.chuckfacts.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
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

class SearchFactsFragment : Fragment(R.layout.search_facts_fragment),
    ChuckFactAdapter.OnItemClickListener {

    private lateinit var viewModel: ChuckFactsViewModel
    lateinit var searchedFactAdapter: ChuckFactAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var etSearch: EditText
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[ChuckFactsViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.search_facts_rv)
        etSearch = view.findViewById(R.id.search_facts_et)
        progressBar = view.findViewById(R.id.search_progress_bar)

        setupRecyclerView()

        var job: Job? = null
        etSearch.addTextChangedListener { editableString ->
            job?.cancel()
            job = MainScope().launch {
                delay(1000L)
                editableString?.let {
                    if (editableString.toString().isNotEmpty()) {
                        viewModel.getSearchedFact(editableString.toString())
                    }
                }
            }
        }

        viewModel.searchFact.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Success -> {
                    progressBar.visibility = View.INVISIBLE
                    response.data?.let { factResponse ->
                        if (factResponse.factList.isEmpty()) {
                            Toast.makeText(context, "No facts found", Toast.LENGTH_LONG).show()
                        } else {
                            searchedFactAdapter.differ.submitList(factResponse.factList)
                        }
                    }
                }

                is Resource.Error -> {
                    progressBar.visibility = View.INVISIBLE
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupRecyclerView() {
        searchedFactAdapter = ChuckFactAdapter(this)
        recyclerView.apply {
            adapter = searchedFactAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onItemClick(position: Int) {
        val clickedFactString = searchedFactAdapter.differ.currentList[position]

        val action =
            SearchFactsFragmentDirections.actionNavigationFactSearchToSelectedSearchedFactFragment(
                clickedFactString
            )
        requireView().findNavController().navigate(action)
    }
}