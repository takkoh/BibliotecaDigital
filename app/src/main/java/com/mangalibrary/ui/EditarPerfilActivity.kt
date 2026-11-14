package com.mangalibrary.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mangalibrary.databinding.ActivityEditarPerfilBinding

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarPerfilBinding

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

        val sharedPrefs = getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)
        cargarDatosActuales(sharedPrefs)

        binding.BtnGuardar.setOnClickListener {
            guardarDatosPerfil(sharedPrefs)
        }
    }

    private fun cargarDatosActuales(sharedPrefs: android.content.SharedPreferences) {
        binding.EtNombres.setText(sharedPrefs.getString("user_name", "Lector de Manga"))
        binding.EtEmail.setText(sharedPrefs.getString("user_email", "lector@mangalibrary.com"))
        binding.EtTelefono.setText(sharedPrefs.getString("user_phone", "+34 600 000 000"))
        binding.EtNacimiento.setText(sharedPrefs.getString("user_birth", "01/01/2000"))
        binding.EtMiembro.setText(sharedPrefs.getString("user_member", "Enero 2026"))
        binding.EtEstadoCuenta.setText(sharedPrefs.getString("user_status", "Activo"))
    }

    private fun guardarDatosPerfil(sharedPrefs: android.content.SharedPreferences) {
        val name = binding.EtNombres.text.toString().trim()
        val email = binding.EtEmail.text.toString().trim()
        val phone = binding.EtTelefono.text.toString().trim()
        val birth = binding.EtNacimiento.text.toString().trim()
        val member = binding.EtMiembro.text.toString().trim()
        val status = binding.EtEstadoCuenta.text.toString().trim()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nombre y Correo no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        sharedPrefs.edit().apply {
            putString("user_name", name)
            putString("user_email", email)
            putString("user_phone", phone)
            putString("user_birth", birth)
            putString("user_member", member)
            putString("user_status", status)
            apply()
        }

        Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
        finish()
    }
}
