package com.helenandrade.app_cardapio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DialogInputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_input)

        val continueButton = findViewById<Button>(R.id.continue_button)
        continueButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.editTextName).text.toString()
            val tableNumber = findViewById<EditText>(R.id.editTextTableNumber).text.toString()

            if (name.isNotEmpty() && tableNumber.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("name", name)
                intent.putExtra("tableNumber", tableNumber)
                startActivity(intent)
            } else {
                showToast("Preencha o nome e o número da mesa.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
