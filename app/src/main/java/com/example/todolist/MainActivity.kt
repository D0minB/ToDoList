package com.example.todolist

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var taskListView: ListView
    private lateinit var taskEditText: EditText
    private lateinit var addButton: Button
    private lateinit var taskAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // lista zadan, pole tekstowe, przycisk
        taskListView = findViewById(R.id.taskListView)
        taskEditText = findViewById(R.id.taskEditText)
        addButton = findViewById(R.id.addButton)

        // adapter listy zadan
        taskAdapter = CustomAdapter(this, R.layout.list_item_layout, mutableListOf())
        taskListView.adapter = taskAdapter

        // listener wcisniecia przycisku "DODAJ"
        addButton.setOnClickListener {
            addTask()
        }

        // listener dla długiego naciśnięcia na element listy
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

            // zaktualizuj liste
            taskAdapter.notifyDataSetChanged()

            // wyczyszczenie pola tekstowego po dodaniu zadania
            taskEditText.text.clear()
        }
    }

    private fun removeTask(position: Int) {
        // usuń zadanie z listy
        taskAdapter.remove(taskAdapter.getItem(position))

        // zaktualizuj liste
        taskAdapter.notifyDataSetChanged()
    }


    private class CustomAdapter(context: AppCompatActivity, resource: Int, objects: List<String>) :
        ArrayAdapter<String>(context, resource, objects) {

    }
}

