package com.mangalibrary

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mangalibrary.Fragmentos.FragmentBusqueda
import com.mangalibrary.Fragmentos.FragmentCuenta
import com.mangalibrary.Fragmentos.FragmentInicio
import com.mangalibrary.Fragmentos.FragmentMisFavoritos
import com.mangalibrary.Fragmentos.FragmentRecomendaciones
import com.mangalibrary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        var isDarkMode = sharedPrefs.getBoolean("dark_mode", AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)

        binding.btnThemeToggle.setOnClickListener {
            isDarkMode = !isDarkMode
            sharedPrefs.edit().putBoolean("dark_mode", isDarkMode).apply()

            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        if (savedInstanceState == null) {
            verFragmentInicio()
        }

        binding.BottomNV.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Item_Inicio -> {
                    verFragmentInicio()
                    true
                }
                R.id.Item_Busqueda -> {
                    verFragmentBusqueda()
                    true
                }
                R.id.Item_Recomendaciones -> {
                    verFragmentRecomendaciones()
                    true
                }
                R.id.Item_Mis_Favoritos -> {
                    verFragmentMisFavoritos()
                    true
                }
                R.id.Item_Cuenta -> {
                    verFragmentCuenta()
                    true
                }
                else -> false
            }
        }
    }

    private fun verFragmentInicio() {
        binding.tituloRL.text = "Inicio"
        val fragment = FragmentInicio()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentInicio")
        fragmentTransition.commit()
    }

    private fun verFragmentBusqueda() {
        binding.tituloRL.text = "Búsqueda"
        val fragment = FragmentBusqueda()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentBusqueda")
        fragmentTransition.commit()
    }

    private fun verFragmentRecomendaciones() {
        binding.tituloRL.text = "Recomendaciones"
        val fragment = FragmentRecomendaciones()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentRecomendaciones")
        fragmentTransition.commit()
    }

    private fun verFragmentMisFavoritos() {
        binding.tituloRL.text = "Mis Favoritos"
        val fragmentMisFavoritos = FragmentMisFavoritos()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragmentMisFavoritos, "FragmentMisFavoritos")
        fragmentTransition.commit()
    }

    private fun verFragmentCuenta() {
        binding.tituloRL.text = "Cuenta"
        val fragment = FragmentCuenta()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentCuenta")
        fragmentTransition.commit()
    }
}
