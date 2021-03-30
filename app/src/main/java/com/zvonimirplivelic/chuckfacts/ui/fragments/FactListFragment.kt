package com.zvonimirplivelic.chuckfacts.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.adapter.ChuckFactAdapter
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsActivity
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsViewModel
import kotlinx.coroutines.launch

class FactListFragment : Fragment(R.layout.fact_list_fragment),
    ChuckFactAdapter.OnItemClickListener {
    private lateinit var viewModel: ChuckFactsViewModel
    lateinit var chuckFactAdapter: ChuckFactAdapter
    lateinit var recyclerView: RecyclerView

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

        val clearListBtn: Button = view.findViewById(R.id.delete_all_facts_btn)

        recyclerView = view.findViewById(R.id.saved_facts_list_rv)
        setupRecyclerView()

        viewModel.getSavedFacts().observe(viewLifecycleOwner, { chuckFacts ->
            chuckFactAdapter.differ.submitList(chuckFacts)
        })

        clearListBtn.setOnClickListener {
            lifecycleScope.launch { clearAllFacts() }
        }
    }

    private fun setupRecyclerView() {
        chuckFactAdapter = ChuckFactAdapter(this)
        recyclerView.apply {
            adapter = chuckFactAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun clearAllFacts() {
        viewModel.deleteAllFacts()
    }

    override fun onItemClick(position: Int) {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.apply {
                setTitle("Delete Fact")
                setMessage("Do you want to delete this Chuck Fact?")
                setIcon(R.drawable.ic_new_chuck_fact)
                setPositiveButton("Yes") { dialog, _ ->
                    viewModel.deleteFact(chuckFactAdapter.differ.currentList[position])
                    dialog.dismiss()
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
            }
            builder?.create()
        }
        alertDialog?.show()
    }
}