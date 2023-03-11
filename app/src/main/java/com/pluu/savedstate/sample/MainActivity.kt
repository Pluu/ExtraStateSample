package com.pluu.savedstate.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.savedstate.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnActivitySample.setOnClickListener {
            val intent = Intent(this, ActivityStateActivity::class.java)
            startActivity(intent)
        }

        binding.btnFragmentSample.setOnClickListener {
            val intent = Intent(this, FragmentStateActivity::class.java)
            startActivity(intent)
        }
    }
}