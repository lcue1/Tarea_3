package com.example.create_acount_bank

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var personName: EditText
    lateinit var identification: EditText
    lateinit var btnGo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        personName = findViewById(R.id.personName)
        identification = findViewById(R.id.identification)
        btnGo = findViewById(R.id.btnGo)

        btnGo.setOnClickListener {
            val personNameCondition = personName.text.toString().isEmpty()
            val identificationCondition = identification.text.toString().length != 10

            val nameValidation = validateEditText(personName, personNameCondition)
            val identifiValidation = validateEditText(identification, identificationCondition)

            if (!nameValidation && !identifiValidation) {
                Toast.makeText(this, "Register success", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Please correct the errors", Toast.LENGTH_SHORT).show()
            }
        }

        personName.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                personName.background = ContextCompat.getDrawable(this, R.color.white)
            } else if (personName.text.toString().isEmpty()) {
                personName.background = ContextCompat.getDrawable(this, R.color.errorEditText)
            }
        }

        identification.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                identification.background = ContextCompat.getDrawable(this, R.color.white)
            } else if (identification.text.toString().length != 10) {
                identification.background = ContextCompat.getDrawable(this, R.color.errorEditText)
            }
        }
    }

    private fun validateEditText(editText: EditText, validation: Boolean): Boolean {
        if (validation) {
            editText.background = ContextCompat.getDrawable(this, R.color.errorEditText)
            return true
        } else {
            editText.background = ContextCompat.getDrawable(this, R.color.white)
            return false
        }
    }
}