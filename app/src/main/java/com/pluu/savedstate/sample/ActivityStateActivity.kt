package com.pluu.savedstate.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.pluu.savedstate.databinding.ActivityStateBinding
import com.pluu.savedstate.sample.model.SampleModel
import com.pluu.utils.extra.ExtraStateful
import com.pluu.utils.extra.extraStateful
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ActivityStateActivity : AppCompatActivity(), ExtraStateful by extraStateful() {
    private lateinit var binding: ActivityStateBinding

    private val extraNonNull by extra<String>()
    private val arrayListCase by extra<ArrayList<String>>()
    private val serializableCase by extra<SampleModel>()

    private val extraNull by extraNullable<String>()
    private val arrayListNullCase by extraNullable<ArrayList<String>>()
    private val serializableNullCase by extraNullable<SampleModel>()

    private var mutableExtraNull by extraNullable<String>()
    private var mutableExtraNonNull by extra<String>()
    private var mutableSerializableCase by extra<SampleModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restore(intent?.extras, savedInstanceState)
        binding = ActivityStateBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        save(outState)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ActivityStateActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        "extraNonNull" to "result1",
                        "arrayListCase" to arrayListOf("A", "B", "C"),
                        "serializableCase" to SampleModel("1"),
                        "mutableExtraNonNull" to "mutable_result1",
                        "mutableSerializableCase" to SampleModel("Mutable")
                    )
                )
            }
            context.startActivity(intent)
        }
    }
}