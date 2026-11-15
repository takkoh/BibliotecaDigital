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
 * Fragmento de Cuenta simplificado para aplicación de Manga,
 * mostrando únicamente el nombre y el correo electrónico del lector.
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

        // Botón Editar Perfil
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
        val sharedPrefs = requireContext().getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)
        loadProfileData(sharedPrefs)
    }

    private fun loadProfileData(sharedPrefs: android.content.SharedPreferences) {
        val name = sharedPrefs.getString("user_name", "Lector de Manga")
        val email = sharedPrefs.getString("user_email", "lector@mangalibrary.com")

        binding.TvNombres.text = name
        binding.TvEmail.text = email
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
