package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import java.io.*

class TasksActivity : AppCompatActivity() {
    private lateinit var taskListView: ListView
    private lateinit var taskEditText: EditText
    private lateinit var addButton: Button
    private lateinit var taskAdapter: ArrayAdapter<String>
    private lateinit var fileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        // lista zadan, pole tekstowe, przycisk
        taskListView = findViewById(R.id.taskListView)
        taskEditText = findViewById(R.id.taskEditText)
        addButton = findViewById(R.id.addButton)

        val listName = intent.getStringExtra("list")
        fileName = "$listName.txt"

        // adapter listy zadan
        taskAdapter = CustomAdapter(this, R.layout.list_item_layout, mutableListOf())
        taskListView.adapter = taskAdapter

        loadTasksFromFile()

        // listener wcisniecia przycisku "DODAJ"
        addButton.setOnClickListener {
            addTask()
        }

        // listener dla długiego naciśnięcia na element listy - usuniecie zadania
        taskListView.setOnItemLongClickListener { _, _, position, _ ->
            removeTask(position)
            true // Zwróć true, aby wskazać, że zdarzenie zostało obsłużone
        }
    }

    private fun addTask() {
        val taskText = taskEditText.text.toString()

        if (taskText.isNotEmpty()) {
            // dodaj zadanie
            taskAdapter.add(taskText)

            // zaktualizuj widok
            taskAdapter.notifyDataSetChanged()

            // zapisz listę zadań do pliku
            saveTasksToFile()

            // wyczyszczenie pola tekstowego po dodaniu zadania
            taskEditText.text.clear()
        }
    }

    private fun removeTask(position: Int) {
        // usuń zadanie z listy
        taskAdapter.remove(taskAdapter.getItem(position))

        // zaktualizuj widok
        taskAdapter.notifyDataSetChanged()

        // zaktualizuj listę zadań w pliku po usunięciu zadania
        saveTasksToFile()
    }

    private fun saveTasksToFile() {
        val file = File(filesDir, fileName)
        val fileOutputStream = FileOutputStream(file)

        for (i in 0 until taskAdapter.count) {
            val task = taskAdapter.getItem(i)
            if (task != null) {
                fileOutputStream.write("$task\n".toByteArray())
            }
        }

        fileOutputStream.close()
    }

    private fun loadTasksFromFile(): MutableList<String> {
        val file = File(filesDir, fileName)
        val taskList = mutableListOf<String>()

        if (file.exists()) {
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                taskAdapter.add(line.orEmpty())
            }

            // zaktualizuj liste
            taskAdapter.notifyDataSetChanged()

            fileInputStream.close()

        }

        return taskList
    }


    private class CustomAdapter(context: AppCompatActivity, resource: Int, objects: List<String>) :
        ArrayAdapter<String>(context, resource, objects) {

    }
}