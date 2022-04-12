package com.zvonimirplivelic.chuckfacts.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.ui.ChuckFactsViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StoredFactFragment : Fragment() {

    private val args by navArgs<StoredFactFragmentArgs>()
    private lateinit var viewModel: ChuckFactsViewModel

    private lateinit var tvStoredFactText: TextView
    private lateinit var tvStoredFactTime: TextView
    private lateinit var ivDeleteStoredFact: ImageView
    private lateinit var ivShareStoredFact: ImageView

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
        ivDeleteStoredFact = view.findViewById(R.id.iv_delete_stored_fact)
        ivShareStoredFact = view.findViewById(R.id.iv_share_stored_fact)

        tvStoredFactText.text = args.selectedFact.value
        tvStoredFactTime.text = "Saved at: $formatTime"

        ivDeleteStoredFact.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.apply {
                setTitle("Delete Fact")
                setMessage("Do you want to delete this fact?")
                setPositiveButton("Delete fact") { _, _ ->
                    val action =
                        StoredFactFragmentDirections.actionStoredFactFragmentToNavigationRandomFact()
                    requireView().findNavController().navigate(action)
                    lifecycleScope.launch {
                        viewModel.deleteFact(args.selectedFact.id)
                    }

                    Toast.makeText(
                        context,
                        "Fact deleted",
                        Toast.LENGTH_LONG
                    ).show()
                }
                setNegativeButton("Cancel") { _, _ ->

                }
                create().show()
            }
        }

        ivShareStoredFact.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, args.selectedFact.value)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Share this fact")
            startActivity(shareIntent)
        }

        return view
    }
}