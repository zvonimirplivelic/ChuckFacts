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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsViewModel

class SelectedSearchedFactFragment : Fragment() {

    private val args by navArgs<StoredFactFragmentArgs>()
    private lateinit var viewModel: ChuckFactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_selected_searched_fact, container, false)

        val chuckFactTextView: TextView = view.findViewById(R.id.tv_stored_fact_text)
        val updatedFactTextView: TextView = view.findViewById(R.id.tv_updated_time)
        val createdFactTextView: TextView = view.findViewById(R.id.tv_created_time)
        val shareImageView: ImageView = view.findViewById(R.id.iv_share_fact)
        val storeImageView: ImageView = view.findViewById(R.id.iv_store_fact)

        viewModel = ViewModelProvider(this)[ChuckFactsViewModel::class.java]

        chuckFactTextView.text = args.selectedFact.value
        updatedFactTextView.text =
            "Updated at: ${(args.selectedFact.updatedAt).take(19)}"
        createdFactTextView.text =
            "Created at: ${(args.selectedFact.createdAt).take(19)}"

        storeImageView.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.apply {
                setTitle("Save Fact")
                setMessage("Do you want to save this fact?")
                setPositiveButton("Save fact") { _, _ ->
                    viewModel.saveFact(args.selectedFact)
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


        return view
    }
}