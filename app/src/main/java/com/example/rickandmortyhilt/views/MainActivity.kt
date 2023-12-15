package com.example.rickandmortyhilt.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rickandmortyhilt.views.characters.RickAndMortyCharacterFragment
import com.example.rickandmortyhilt.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, RickAndMortyCharacterFragment.newInstance())
                .addToBackStack(null).commit()
        }
    }
}