package mhk.app.currencyconverter.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mhk.app.currencyconverter.R
import mhk.app.currencyconverter.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}