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
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.model.ChuckFact
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsViewModel
import com.zvonimirplivelic.chuckfacts.util.Resource

class SingleFactFragment : Fragment() {
    private lateinit var viewModel: ChuckFactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.single_fact_fragment, container, false)


        viewModel = ViewModelProvider(this)[ChuckFactsViewModel::class.java]
        viewModel.getRandomFact()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var storedResponse: ChuckFact? = null
        val chuckFactTextView: TextView = view.findViewById(R.id.chuck_fact_tv)
        val progressBar: ProgressBar = view.findViewById(R.id.random_progress_bar)
        val shareImageView: ImageView = view.findViewById(R.id.share_fact_iv)
        val storeImageView: ImageView = view.findViewById(R.id.store_fact_iv)

        viewModel.randomFact.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    progressBar.isVisible = false
                    response.data?.let { factResponse ->
                        storedResponse = factResponse
                        chuckFactTextView.text = storedResponse!!.value
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
            viewModel.saveFact(storedResponse!!)
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