package com.mangalibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        verFragmentInicio()

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
        val fragment = FragmentMisFavoritos()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentMisFavoritos")
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
