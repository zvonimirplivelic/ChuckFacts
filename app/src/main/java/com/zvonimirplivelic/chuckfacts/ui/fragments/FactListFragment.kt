package com.zvonimirplivelic.chuckfacts.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.adapter.ChuckFactAdapter
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsViewModel
import kotlinx.coroutines.launch

class FactListFragment : Fragment(R.layout.fragment_fact_list),
    ChuckFactAdapter.OnItemClickListener {
    private lateinit var viewModel: ChuckFactsViewModel
    lateinit var chuckFactAdapter: ChuckFactAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[ChuckFactsViewModel::class.java]

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clearListBtn: Button = view.findViewById(R.id.btn_delete_all_facts)

        recyclerView = view.findViewById(R.id.rv_saved_facts_list)
        setupRecyclerView()

        viewModel.getSavedFacts().observe(viewLifecycleOwner) { chuckFacts ->
            chuckFactAdapter.differ.submitList(chuckFacts)
        }

        clearListBtn.setOnClickListener {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)

                builder.apply {
                    setTitle("Delete All Facts")
                    setMessage("Do you want to delete all Chuck Facts?")
                    setIcon(R.drawable.ic_new_chuck_fact)
                    setPositiveButton("Yes") { dialog, _ ->
                        lifecycleScope.launch { clearAllFacts() }
                        dialog.dismiss()
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.cancel()
                    }
                }
                builder.create()
            }
            alertDialog?.show()
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
        val clickedFactString = chuckFactAdapter.differ.currentList[position]

        val action = FactListFragmentDirections.actionNavigationFactListToStoredFactFragment(clickedFactString)
        requireView().findNavController().navigate(action)
//
//        val alertDialog: AlertDialog? = activity?.let {
//            val builder = AlertDialog.Builder(it)
//
//            builder.apply {
//                setTitle("Delete Fact")
//                setMessage("Do you want to delete this Chuck Fact?")
//                setIcon(R.drawable.ic_new_chuck_fact)
//                setPositiveButton("Yes") { dialog, _ ->
//                    viewModel.deleteFact(clickedFactString.id)
//                    dialog.dismiss()
//                }
//                setNegativeButton("No") { dialog, _ ->
//                    dialog.cancel()
//                }
//            }
//            builder?.create()
//        }
//        alertDialog?.show()
    }
}
