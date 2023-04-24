package com.siri_hate.besthack23

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import java.io.BufferedReader
import java.io.InputStreamReader


class WelcomeFragment : Fragment() {

    private lateinit var filenameTextView: TextView

    private val fileSelector = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val uri = result.data?.data
            val fileName = getFileName(uri)
            filenameTextView.text = fileName
            val fileContent = getFileContent(uri) // Содержимое файла
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        filenameTextView = view.findViewById(R.id.welcome_fragment_filename)
        val loadFileBtn = view.findViewById<Button>(R.id.welcome_fragment_load_file_btn)
        loadFileBtn.setOnClickListener { openFileChooser() }

        return view
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("text/plain", "application/fb2", "application/epub+zip"))

        try {
            fileSelector.launch(Intent.createChooser(intent, "Выберите файл для загрузки"))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Установите файловый менеджер!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileName(uri: Uri?): String {
        requireContext().contentResolver.query(uri!!, null, null, null, null)?.let { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            val name = cursor.getString(nameIndex)
            cursor.close()
            return name
        }
        return ""
    }

    private fun getFileContent(uri: Uri?): String {
        val inputStream = requireContext().contentResolver.openInputStream(uri!!)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        inputStream?.close()
        reader.close()
        return stringBuilder.toString()
    }

}