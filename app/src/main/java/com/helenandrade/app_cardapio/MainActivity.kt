package com.helenandrade.app_cardapio

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

open class Prato(
    var nome: String,
    var preço: Double,
    var tempoPreparo: Int,
    var tipo: String,
    var ativo: Boolean = false
) {
    fun ativar() {
        ativo = true
    }

    fun desativar() {
        ativo = false
    }
}

class Entrada(nome: String, preço: Double, tempoPreparo: Int) :
    Prato(nome, preço, tempoPreparo, "Entrada")

class PratoPrincipal(nome: String, preço: Double, tempoPreparo: Int) :
    Prato(nome, preço, tempoPreparo, "Prato Principal")

class Sobremesa(nome: String, preço: Double, tempoPreparo: Int) :
    Prato(nome, preço, tempoPreparo, "Sobremesa")

class Bebida(nome: String, preço: Double, tempoPreparo: Int) :
    Prato(nome, preço, tempoPreparo, "Bebida")

class MainActivity : AppCompatActivity() {

    private var total: Double = 0.0

    private val listaDePratos = listOf(
        Entrada("SASHIMI", 42.0, 10),
        Entrada("URAMAKI", 38.0, 15),
        Entrada("NIGIRI", 35.0, 10),
        Entrada("YAKIMONO", 39.0, 15),
        PratoPrincipal("SUKIYAKI", 65.0, 25),
        PratoPrincipal("EBI WONG", 74.0, 15),
        PratoPrincipal("TAKO", 78.0, 20),
        PratoPrincipal("TEPPAN", 67.0, 20),
        Sobremesa("SWEET SUSHI", 31.0, 10),
        Sobremesa("CHOCOLATE SASHIMI", 32.0, 10),
        Sobremesa("MIL FILO", 28.0, 15),
        Sobremesa("CHOCO BANANA", 30.0, 15),
        Bebida("MOSCOW GIN", 28.0, 5),
        Bebida("SUCO DE ABACAXI COM HORTELÃS", 12.0, 5),
        Bebida("SAMURAI MARTINI", 20.0, 5),
        Bebida("CHÁ GELADO DE HIBISCO", 17.0, 5)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val name = intent.getStringExtra("name")
        val tableNumber = intent.getStringExtra("tableNumber")

        val greetingTextView = findViewById<TextView>(R.id.text_client_name)
        greetingTextView.text = "Bem-vindo(a), $name!"

        val numberTextView = findViewById<TextView>(R.id.text_client_number)
        numberTextView.text = "Número da mesa: $tableNumber"

        setupPratoToggle(listaDePratos[0], R.id.checkbox_appetizer_1)
        setupPratoToggle(listaDePratos[1], R.id.checkbox_appetizer_2)
        setupPratoToggle(listaDePratos[2], R.id.checkbox_appetizer_3)
        setupPratoToggle(listaDePratos[3], R.id.checkbox_appetizer_4)

        setupPratoToggle(listaDePratos[4], R.id.checkbox_maincourse_1)
        setupPratoToggle(listaDePratos[5], R.id.checkbox_maincourse_2)
        setupPratoToggle(listaDePratos[6], R.id.checkbox_maincourse_3)
        setupPratoToggle(listaDePratos[7], R.id.checkbox_maincourse_4)

        setupPratoToggle(listaDePratos[8], R.id.checkbox_dessert_1)
        setupPratoToggle(listaDePratos[9], R.id.checkbox_dessert_2)
        setupPratoToggle(listaDePratos[10], R.id.checkbox_dessert_3)
        setupPratoToggle(listaDePratos[11], R.id.checkbox_dessert_4)

        setupPratoToggle(listaDePratos[12], R.id.checkbox_beverage_1)
        setupPratoToggle(listaDePratos[13], R.id.checkbox_beverage_2)
        setupPratoToggle(listaDePratos[14], R.id.checkbox_beverage_3)
        setupPratoToggle(listaDePratos[15], R.id.checkbox_beverage_4)

        val confirmButton = findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        if (total > 0) {
            val alertDialogBuilder = AlertDialog.Builder(this)

            alertDialogBuilder.setTitle("Confirmação")

            val pratosEscolhidos = getPratosEscolhidos()
            val preferences = findViewById<EditText>(R.id.editTextPreferences).text.toString()

            val message =
                "Você deseja confirmar o pedido?\n\n$pratosEscolhidos\nPreferências: $preferences\n\nTotal: R$ $total"

            alertDialogBuilder.setMessage(message)

            alertDialogBuilder.setPositiveButton("Sim") { _, _ ->
                showToastLong("Seu pedido foi enviado para o balcão do restaurante!")
            }

            alertDialogBuilder.setNegativeButton("Não") { _, _ ->
                showToastLong("Pedido não confirmado.")
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        } else {
            showToastShort("Selecione pelo menos um prato antes de confirmar o pedido.")
        }
    }

    private fun setupPratoToggle(prato: Prato, checkBoxId: Int) {
        val checkBox = findViewById<CheckBox>(checkBoxId)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                prato.ativar()
                showToastShort("Prato ${prato.nome} incluído.")
            } else {
                prato.desativar()
                showToastShort("Prato ${prato.nome} retirado.")
            }
            calcularTotal()
        }
    }

    private fun getPratosEscolhidos(): String {
        val pratosEscolhidos = StringBuilder()

        for (prato in listaDePratos) {
            if (prato.ativo) {
                pratosEscolhidos.append("${prato.nome} - R$ ${prato.preço}\n")
            }
        }

        return pratosEscolhidos.toString()
    }

    private fun showToastLong(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showToastShort(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun calcularTotal() {
        total = listaDePratos.filter { it.ativo }.sumByDouble { it.preço }
        updateTotalUI(total)
    }

    private fun updateTotalUI(newTotal: Double) {
        val textViewTotal = findViewById<TextView>(R.id.textViewTotal)
        textViewTotal.text = "Total: R$ $newTotal"
    }
}
