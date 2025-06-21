package com.greatappmobile.myapplication

import android.os.Bundle
import com.greatappmobile.myapplication.databinding.ActivityMainBinding
import com.greatappmobile.myapplication.defaults.DefaultActivity

class MainActivity : DefaultActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setScreenOrientation(isPortrait= true)

        binding.textView.setOnClickListener {
            showLocaleDialog()
        }

    }

}