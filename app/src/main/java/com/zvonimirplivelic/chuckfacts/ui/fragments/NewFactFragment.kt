package com.zvonimirplivelic.chuckfacts.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.ui.viewmodel.NewFactViewModel

class NewFactFragment : Fragment() {
    private lateinit var chuckFactTextView: TextView
    private lateinit var progressBar: ProgressBar

    companion object {
        fun newInstance() = NewFactFragment()
    }

    private lateinit var viewModel: NewFactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.new_fact_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chuckFactTextView = view.findViewById(R.id.chuck_fact_tv)
        progressBar = view.findViewById(R.id.progress_bar)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewFactViewModel::class.java)
        // TODO: Use the ViewModel
    }

}