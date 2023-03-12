package com.pluu.savedstate.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.pluu.savedstate.sample.databinding.FragmentMainBinding
import com.pluu.savedstate.sample.model.SampleModel
import com.pluu.utils.extra.ExtraStateful
import com.pluu.utils.extra.extraStateful
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FragmentStateFragment : Fragment(), ExtraStateful by extraStateful() {

    private val extraNonNull by extra<String>()
    private val arrayListCase by extra<ArrayList<String>>()
    private val serializableCase by extra<SampleModel>()

    private val extraNull by extraNullable<String>()
    private val arrayListNullCase by extraNullable<ArrayList<String>>()
    private val serializableNullCase by extraNullable<SampleModel>()

    private var mutableExtraNull by extraNullable<String>()
    private var mutableExtraNonNull by extra<String>()
    private var mutableSerializableCase by extra<SampleModel>()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restore(arguments, savedInstanceState)
        binding.btnUpdateState.setOnClickListener {
            val ldt = LocalDateTime.now()
            val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            mutableExtraNonNull = ldt.format(dateFormat)
            mutableExtraNull = ldt.format(dateFormat)
            mutableSerializableCase = SampleModel(ldt.format(dateFormat))

            updateText()
        }

        updateText()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        save(outState)
    }

    private fun updateText() {
        binding.tvNonNullLabel.text = """
            String: $extraNonNull
            ArrayList: $arrayListCase
            Serializable: $serializableCase
        """.trimIndent()

        binding.tvNullLabel.text = """
            String: $extraNull
            ArrayList: $arrayListNullCase
            Serializable: $serializableNullCase
        """.trimIndent()

        binding.tvMutableLabel.text = """
            String: $mutableExtraNonNull
            String Nullable: $mutableExtraNull
            Serializable : $mutableSerializableCase
        """.trimIndent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FragmentStateFragment().apply {
            arguments = bundleOf(
                "extraNonNull" to "result1",
                "arrayListCase" to arrayListOf("A", "B", "C"),
                "serializableCase" to SampleModel("1"),
                "mutableExtraNonNull" to "mutable_result1",
                "mutableSerializableCase" to SampleModel("Mutable")
            )
        }
    }
}