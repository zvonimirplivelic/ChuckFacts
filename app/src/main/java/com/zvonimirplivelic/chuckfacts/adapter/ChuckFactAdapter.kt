package com.zvonimirplivelic.chuckfacts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.model.ChuckFact
import com.zvonimirplivelic.chuckfacts.ui.fragments.FactListFragmentDirections

class ChuckFactAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ChuckFactAdapter.ChuckFactViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ChuckFact>() {
        override fun areItemsTheSame(oldItem: ChuckFact, newItem: ChuckFact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChuckFact, newItem: ChuckFact): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChuckFactViewHolder {
        return ChuckFactViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fact_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChuckFactViewHolder, position: Int) {
        val chuckFact = differ.currentList[position]
        holder.itemView.apply {
            val tvChuckFactListItem: TextView = findViewById(R.id.chuck_fact_list_item_tv)
            tvChuckFactListItem.text = chuckFact.value
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ChuckFactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}

