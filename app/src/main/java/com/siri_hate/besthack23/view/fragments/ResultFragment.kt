package com.siri_hate.besthack23.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siri_hate.besthack23.R
import com.siri_hate.besthack23.view.adapters.ResultListAdapter

class ResultFragment : Fragment() {

    private lateinit var analyzeTimeTextView: TextView
    private lateinit var resultList: RecyclerView
    private lateinit var adapter: ResultListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        analyzeTimeTextView = view.findViewById(R.id.fragment_result_analyze_time_value)
        resultList = view.findViewById(R.id.fragment_result_words_list)

        @Suppress("UNCHECKED_CAST")
        val result = arguments?.getSerializable("result") as List<Triple<String, Int, Int>>? ?: emptyList()
        val processingTime = arguments?.getLong("processingTime", 0) ?: 0

        analyzeTimeTextView.text = resources.getString(R.string.analyze_time_text, processingTime)

        adapter = ResultListAdapter(result, requireContext())
        resultList.adapter = adapter

        return view
    }
}