package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.*

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var listEditText: EditText
    private lateinit var addButton: Button
    private lateinit var listsAdapter: ArrayAdapter<String>

    // w tym pliku zapisane listy
    private val fileName = "lists.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // lista list zadan, pole tekstowe, przycisk
        listView = findViewById(R.id.taskListView)
        listEditText = findViewById(R.id.taskEditText)
        addButton = findViewById(R.id.addButton)

        // adapter listy zadan
        listsAdapter = CustomAdapter(this, R.layout.list_item_layout, mutableListOf())
        listView.adapter = listsAdapter

        // wczytaj zapisane w pliku txt listy zadan
        loadListsFromFile()

        // listener wcisniecia przycisku "DODAJ"
        addButton.setOnClickListener {
            addList()
        }

        // listener dla krótkiego naciśnięcia na element listy
        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, TasksActivity::class.java)
            intent.putExtra("list", listView.getItemAtPosition(position).toString())
            startActivity(intent)
            true
        }

        // listener dla długiego naciśnięcia na element listy
        listView.setOnItemLongClickListener { _, _, position, _ ->
            removeList(position)
            true // Zwróć true, aby wskazać, że zdarzenie zostało obsłużone
        }
    }

    private fun addList() {
        val taskText = listEditText.text.toString()

        if (taskText.isNotEmpty()) {
            // dodaj liste zadan
            listsAdapter.add(taskText)

            // zaktualizuj widok
            listsAdapter.notifyDataSetChanged()

            // zapisz listę zadań do pliku
            saveListsToFile()

            // wyczyszczenie pola tekstowego po dodaniu listy zadan
            listEditText.text.clear()
        }
    }

    private fun removeList(position: Int) {
        // usuń liste zadan
        listsAdapter.remove(listsAdapter.getItem(position))

        // zaktualizuj widok
        listsAdapter.notifyDataSetChanged()

        // zaktualizuj listę zadań w pliku po usunięciu
        saveListsToFile()
    }

    private fun saveListsToFile() {
        val file = File(filesDir, fileName)
        val fileOutputStream = FileOutputStream(file)

        for (i in 0 until listsAdapter.count) {
            val task = listsAdapter.getItem(i)
            if (task != null) {
                fileOutputStream.write("$task\n".toByteArray())
            }
        }

        fileOutputStream.close()
    }

    private fun loadListsFromFile(): MutableList<String> {
        val file = File(filesDir, fileName)
        val taskList = mutableListOf<String>()

        if (file.exists()) {
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                listsAdapter.add(line.orEmpty())
            }

            // zaktualizuj widok
            listsAdapter.notifyDataSetChanged()

            fileInputStream.close()

        }

        return taskList
    }


    private class CustomAdapter(context: AppCompatActivity, resource: Int, objects: List<String>) :
        ArrayAdapter<String>(context, resource, objects) {

    }
}

