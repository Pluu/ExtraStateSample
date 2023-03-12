package com.pluu.utils.extra.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.pluu.savedstate.sample.model.SampleModel
import com.pluu.utils.extra.ExtraStateful
import com.pluu.utils.extra.extraStateful

class ExtraTestFragment : Fragment(), ExtraStateful by extraStateful() {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FrameLayout(layoutInflater.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restore(arguments, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        save(outState)
    }
}