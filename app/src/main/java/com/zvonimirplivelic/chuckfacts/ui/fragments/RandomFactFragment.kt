package com.zvonimirplivelic.chuckfacts.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.model.ChuckFact
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsViewModel
import com.zvonimirplivelic.chuckfacts.util.Resource

class RandomFactFragment : Fragment() {
    private lateinit var viewModel: ChuckFactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_random_fact, container, false)


        viewModel = ViewModelProvider(this)[ChuckFactsViewModel::class.java]
        viewModel.getRandomFact()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var storedResponse: ChuckFact? = null
        val chuckFactTextView: TextView = view.findViewById(R.id.tv_stored_fact_text)
        val updatedFactTextView: TextView = view.findViewById(R.id.tv_updated_time)
        val createdFactTextView: TextView = view.findViewById(R.id.tv_created_time)
        val progressBar: ProgressBar = view.findViewById(R.id.random_progress_bar)
        val shareImageView: ImageView = view.findViewById(R.id.iv_share_fact)
        val storeImageView: ImageView = view.findViewById(R.id.iv_store_fact)

        viewModel.randomFact.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    progressBar.isVisible = false
                    response.data?.let { factResponse ->
                        storedResponse = factResponse
                        chuckFactTextView.text = storedResponse!!.value
                        updatedFactTextView.text = "Updated at: ${(storedResponse!!.updatedAt).take(19)}"
                        createdFactTextView.text = "Created at: ${(storedResponse!!.createdAt).take(19)}"
                    }
                }

                is Resource.Error -> {
                    progressBar.isVisible = false
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    progressBar.isVisible = true
                }
            }
        }

        storeImageView.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.apply {
                setTitle("Save Fact")
                setMessage("Do you want to save this fact?")
                setPositiveButton("Save fact") { _, _ ->
                    viewModel.saveFact(storedResponse!!)
                    Toast.makeText(
                        context,
                        "Fact saved",
                        Toast.LENGTH_LONG
                    ).show()
                }
                setNegativeButton("Cancel") { _, _ ->

                }
                create().show()
            }
        }

        shareImageView.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, chuckFactTextView.text)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Share this fact")
            startActivity(shareIntent)
        }
    }
}