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
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsViewModel
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsActivity
import com.zvonimirplivelic.chuckfacts.util.Resource

class NewFactFragment : Fragment(R.layout.new_fact_fragment) {

    private lateinit var viewModel: ChuckFactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ((activity) as ChuckFactsActivity).viewModel
        viewModel.getRandomFact()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chuckFactTextView: TextView = view.findViewById(R.id.chuck_fact_tv)
        val progressBar: ProgressBar = view.findViewById(R.id.random_progress_bar)
        val shareImageView: ImageView = view.findViewById(R.id.share_fact_iv)

        viewModel.randomFact.observe(viewLifecycleOwner, { response ->

            when (response) {

                is Resource.Success -> {
                    progressBar.isVisible = false
                    response.data?.let { factResponse ->
                        chuckFactTextView.text = factResponse.value
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
        })

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