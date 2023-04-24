package com.siri_hate.besthack23.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siri_hate.besthack23.R

class ResultListAdapter(
    private val result: List<Triple<String, Int, Int>>,
    private val context: Context
    ) :
    RecyclerView.Adapter<ResultListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.result_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (word, wordLength, count) = result[position]
        holder.wordTextView.text = word
        val numLettersString = context.getString(R.string.word_length, wordLength)
        holder.lettersNumTextView.text = numLettersString
        val findNumString = context.getString(R.string.word_find_num, count)
        holder.wordFindNumTextView.text = findNumString
    }

    override fun getItemCount() = result.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordTextView: TextView = itemView.findViewById(R.id.result_item_word_value)
        val lettersNumTextView: TextView = itemView.findViewById(R.id.result_item_list_letters_num_value)
        val wordFindNumTextView: TextView = itemView.findViewById(R.id.result_item_list_word_find_num_value)
    }
}