package com.mangalibrary.Fragmentos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mangalibrary.databinding.FragmentCuentaBinding
import com.mangalibrary.ui.EditarPerfilActivity

/**
 * Fragmento de Cuenta basado en la estructura de la App Agenda,
 * mostrando información detallada del perfil y permitiendo abrir la actividad
 * de edición de perfil para actualizar todos los campos del usuario.
 */
class FragmentCuenta : Fragment() {

    private var _binding: FragmentCuentaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCuentaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón Editar Perfil (Abre EditarPerfilActivity para editar todos los campos)
        binding.BtnEditarPerfil.setOnClickListener {
            val intent = Intent(requireContext(), EditarPerfilActivity::class.java)
            startActivity(intent)
        }

        // Botón Cerrar Sesión
        binding.BtnCerrarSesion.setOnClickListener {
            Toast.makeText(requireContext(), "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargar los datos actualizados cada vez que se regresa al fragmento
        val sharedPrefs = requireContext().getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)
        loadProfileData(sharedPrefs)
    }

    private fun loadProfileData(sharedPrefs: android.content.SharedPreferences) {
        val name = sharedPrefs.getString("user_name", "Lector de Manga")
        val email = sharedPrefs.getString("user_email", "lector@mangalibrary.com")
        val birth = sharedPrefs.getString("user_birth", "01/01/2000")
        val member = sharedPrefs.getString("user_member", "Enero 2026")
        val phone = sharedPrefs.getString("user_phone", "+34 600 000 000")
        val status = sharedPrefs.getString("user_status", "Activo")

        binding.TvNombres.text = name
        binding.TvEmail.text = email
        binding.TvNacimiento.text = birth
        binding.TvMiembro.text = member
        binding.TvTelefono.text = phone
        binding.TvEstadoCuenta.text = status
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
