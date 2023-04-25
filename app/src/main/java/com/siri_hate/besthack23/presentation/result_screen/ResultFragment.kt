package com.siri_hate.besthack23.presentation.result_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.siri_hate.besthack23.R
import com.siri_hate.besthack23.domain.model.AnalyzedWord
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private lateinit var analyzeTimeTextView: TextView
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var adapter: ResultListAdapter
    private var analyzedWordsList: ArrayList<AnalyzedWord> = arrayListOf<AnalyzedWord>()
    private val viewModel: ResultViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        analyzeTimeTextView = view.findViewById(R.id.fragment_result_analyze_time_value)
        resultRecyclerView = view.findViewById(R.id.fragment_result_words_list)

        //val fileContent = arguments?.getString("fileContent")
        processFile()
        collectAnalyzedWordsFlow()
        collectComputationTimeFlow()
        adapter = ResultListAdapter(analyzedWordsList, requireContext())
        resultRecyclerView.adapter = adapter

        return view
    }

    private fun processFile(){
        val fileContent = arguments?.getString("fileContent") ?: ""
        viewModel.analyzeFileContent(fileContent)
    }

    private fun collectAnalyzedWordsFlow() = lifecycleScope.launchWhenStarted {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.analyzedWords.collectLatest { result ->
                analyzedWordsList.clear()
                analyzedWordsList.addAll(result)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun collectComputationTimeFlow() = lifecycleScope.launchWhenStarted {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.computationTime.collectLatest {
                analyzeTimeTextView.text = resources.getString(R.string.analyze_time_text, it)
            }
        }
    }

}