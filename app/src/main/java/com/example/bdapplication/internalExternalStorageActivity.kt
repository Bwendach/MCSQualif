package com.example.bdapplication

import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract.Directory
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bdapplication.databinding.ActivityInternalExternalStorageBinding
import java.io.File
import java.io.FileOutputStream

class internalExternalStorageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInternalExternalStorageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityInternalExternalStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val internalPath = "data/data/com.example.bdapplication"
        val interalDirectory = File(internalPath)

        val externalDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "com.example.bdapplication")


        checkDir(externalDirectory)

        binding.btnInput.setOnClickListener {
            val fileName = binding.etFileName.text.toString()
            val fileContent = binding.etFileContent.text.toString()
            inputFile(fileName, fileContent, externalDirectory)
        }

        binding.btnShow.setOnClickListener {
            val fileName = binding.etFileNameToBeShown.text.toString()
            showFileContent(fileName, externalDirectory)
        }

    }

    private fun showFileContent(fileName: String, directory: File) {
        if(fileName.isEmpty()) {
            Toast.makeText(this, "File name to be shown cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedFile = File(directory, fileName)
        if(!selectedFile.exists()) {
            Toast.makeText(this, "File doesn't exist", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedFileContent = selectedFile.readText()
        Toast.makeText(this, "File content of $fileName: $selectedFileContent", Toast.LENGTH_SHORT).show()

    }

    private fun inputFile(fileName: String, fileContent: String, directory: File) {
        if(fileName.isEmpty() || fileContent.isEmpty()) {
            Toast.makeText(this, "File name and content cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val newFile = File(directory, fileName)

        val fileOutput = FileOutputStream(newFile)
        fileOutput.write(fileContent.toByteArray())
        fileOutput.close()

        binding.etFileName.text.clear()
        binding.etFileContent.text.clear()
        Toast.makeText(this, "Input file success", Toast.LENGTH_SHORT).show()
    }

    private fun checkDir(directory: File) {
        if(!directory.exists()) {
            directory.mkdirs()
        }
    }
}