package com.zvonimirplivelic.chuckfacts.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.zvonimirplivelic.chuckfacts.R

class SelectedSearchedFactFragment : Fragment() {
    private val args by navArgs<StoredFactFragmentArgs>()

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

        chuckFactTextView.text = args.selectedFact.value
        updatedFactTextView.text =
            "Updated at: ${(args.selectedFact.updatedAt).take(19)}"
        createdFactTextView.text =
            "Created at: ${(args.selectedFact.createdAt).take(19)}"


        return view
    }
}