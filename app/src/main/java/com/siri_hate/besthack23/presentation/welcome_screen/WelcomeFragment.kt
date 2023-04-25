package com.siri_hate.besthack23.presentation.welcome_screen

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.siri_hate.besthack23.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.io.Serializable

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private lateinit var filenameTextView: TextView
    private lateinit var fileContent: String
    private val viewModel: WelcomeViewModel by viewModels()

    private val fileSelector =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val uri = result.data?.data
                viewModel.getFileName(uri)
                viewModel.getFileContent(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        filenameTextView = view.findViewById(R.id.welcome_fragment_filename)
        val loadFileBtn = view.findViewById<Button>(R.id.welcome_fragment_load_file_btn)
        loadFileBtn.setOnClickListener { openFileChooser() }

        val startAnalysisBtn = view.findViewById<Button>(R.id.welcome_fragment_start_analysis_btn)
        startAnalysisBtn.setOnClickListener { navigateToResultFragmentAndRunAnalyze() }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectFileNameFlow()
        collectFileContentFlow()
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(
            Intent.EXTRA_MIME_TYPES,
            arrayOf("text/plain", "application/fb2", "application/epub+zip")
        )

        try {
            fileSelector.launch(Intent.createChooser(intent, "Выберите файл для загрузки"))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Установите файловый менеджер!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun collectFileNameFlow() = lifecycleScope.launchWhenStarted {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.fileName.collectLatest {
                filenameTextView.text = it
            }
        }
    }

    private fun collectFileContentFlow() = lifecycleScope.launchWhenStarted {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.fileContent.collectLatest {
                fileContent = it
            }
        }
    }


    private fun navigateToResultFragmentAndRunAnalyze() {
        if (fileContent.isNotBlank()) {
            // Передаём результат и время обработки через navigation action
            val bundle = Bundle()
            bundle.putString("fileContent", fileContent)
            findNavController().navigate(R.id.action_welcomeFragment_to_resultFragment, bundle)
        }
    }
}