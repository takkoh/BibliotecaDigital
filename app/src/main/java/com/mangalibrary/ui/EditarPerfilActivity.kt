package com.mangalibrary.ui

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mangalibrary.databinding.ActivityEditarPerfilBinding

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarPerfilBinding
    private val langOptions = listOf("es", "en", "ja", "all")
    private val langDisplayNames = mapOf("es" to "Español (es)", "en" to "Inglés (en)", "ja" to "Japonés (ja)", "all" to "Todos (all)")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarEditarPerfil)
        supportActionBar?.apply {
            title = "Editar Perfil"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbarEditarPerfil.setNavigationOnClickListener {
            finish()
        }

        setupLangDropdown()

        val sharedPrefs = getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)
        cargarDatosActuales(sharedPrefs)

        binding.BtnGuardar.setOnClickListener {
            guardarDatosPerfil(sharedPrefs)
        }
    }

    private fun setupLangDropdown() {
        val displayList = langOptions.map { langDisplayNames[it] ?: it }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, displayList)
        binding.autoCompletePreferredLang.setAdapter(adapter)
    }

    private fun cargarDatosActuales(sharedPrefs: android.content.SharedPreferences) {
        binding.EtNombres.setText(sharedPrefs.getString("user_name", "Lector de Manga"))
        binding.EtEmail.setText(sharedPrefs.getString("user_email", "lector@mangalibrary.com"))

        val savedLang = sharedPrefs.getString("preferred_lang", "es") ?: "es"
        val displayName = langDisplayNames[savedLang] ?: "Español (es)"
        binding.autoCompletePreferredLang.setText(displayName, false)
    }

    private fun guardarDatosPerfil(sharedPrefs: android.content.SharedPreferences) {
        val name = binding.EtNombres.text.toString().trim()
        val email = binding.EtEmail.text.toString().trim()
        val selectedDisplay = binding.autoCompletePreferredLang.text.toString()

        val selectedLang = langDisplayNames.entries.firstOrNull { it.value == selectedDisplay }?.key ?: "es"

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nombre y Correo no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        sharedPrefs.edit().apply {
            putString("user_name", name)
            putString("user_email", email)
            putString("preferred_lang", selectedLang)
            apply()
        }

        Toast.makeText(this, "Perfil e idioma preferido actualizados correctamente", Toast.LENGTH_SHORT).show()
        finish()
    }
}
