package com.pluu.savedstate.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.savedstate.sample.databinding.ActivityStateBinding

class ActivityStateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStateBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}