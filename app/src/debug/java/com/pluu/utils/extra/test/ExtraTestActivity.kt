package com.pluu.utils.extra.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.pluu.savedstate.sample.model.SampleModel
import com.pluu.utils.extra.ExtraStateful
import com.pluu.utils.extra.extraStateful

class ExtraTestActivity : ComponentActivity(), ExtraStateful by extraStateful() {
    val extraNonNull by extra<String>()
    val arrayListCase by extra<ArrayList<String>>()
    val serializableCase by extra<SampleModel>()

    val extraNull by extraNullable<String>()
    val arrayListNullCase by extraNullable<ArrayList<String>>()
    val serializableNullCase by extraNullable<SampleModel>()

    var mutableExtraNonNull by extra<String>()
    var mutableExtraNull by extraNullable<String>()
    var mutableArrayListCase by extra<List<String>>()
    var mutableSerializableCase by extra<SampleModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restore(intent?.extras, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        save(outState)
    }
}