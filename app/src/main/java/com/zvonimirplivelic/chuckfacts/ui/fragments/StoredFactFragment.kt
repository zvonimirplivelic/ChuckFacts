package com.zvonimirplivelic.chuckfacts.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsViewModel
import java.text.SimpleDateFormat
import java.util.*

class StoredFactFragment : Fragment() {

    private val args by navArgs<StoredFactFragmentArgs>()
    private lateinit var viewModel: ChuckFactsViewModel

    private lateinit var tvStoredFactText: TextView
    private lateinit var tvStoredFactTime: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stored_fact, container, false)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)
        val formatTime = sdf.format(args.selectedFact.savedAt)
        viewModel = ViewModelProvider(this)[ChuckFactsViewModel::class.java]

        tvStoredFactText = view.findViewById(R.id.tv_stored_fact_text)
        tvStoredFactTime = view.findViewById(R.id.tv_stored_time)

        tvStoredFactText.text = args.selectedFact.value
        tvStoredFactTime.text = "Saved at: $formatTime"
        return view
    }
}