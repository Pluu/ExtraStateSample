package com.pluu.savedstate.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FragmentStateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_state)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FragmentStateFragment.newInstance())
                .commitNow()
        }
    }
}