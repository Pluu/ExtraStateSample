package com.pluu.utils.extra

import android.content.Intent
import androidx.core.os.bundleOf
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pluu.savedstate.sample.model.SampleModel
import com.pluu.utils.extra.test.ExtraTestActivity
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RunWith(AndroidJUnit4::class)
class ActivityStateActivityTest {

    @get:Rule
    val rule = ActivityScenarioRule<ExtraTestActivity>(
        Intent(
            ApplicationProvider.getApplicationContext(),
            ExtraTestActivity::class.java
        ).apply {
            putExtras(
                bundleOf(
                    "extraNonNull" to "result1",
                    "arrayListCase" to arrayListOf("A", "B", "C"),
                    "serializableCase" to SampleModel("A"),
                    "mutableExtraNonNull" to "mutable_result1",
                    "mutableArrayListCase" to arrayListOf(
                        "mutable_1",
                        "mutable_2",
                        "mutable_3"
                    ),
                    "mutableSerializableCase" to SampleModel("mutable")
                )
            )
        }
    )

    @Test
    fun t1() {
        lateinit var activity: ExtraTestActivity
        rule.scenario.onActivity {
            activity = it
        }

        // Pre-condition check
        // Check, Non-null
        assertEquals("result1", activity.extraNonNull)
        assertArrayEquals(
            arrayListOf("A", "B", "C").toArray(),
            activity.arrayListCase.toArray()
        )
        assertEquals(SampleModel("A"), activity.serializableCase)

        // Check, Nullable
        assertNull(activity.extraNull)
        assertNull(activity.arrayListNullCase)
        assertNull(activity.serializableNullCase)

        // Check, Mutable
        assertEquals("mutable_result1", activity.mutableExtraNonNull)
        assertNull(activity.mutableExtraNull)
        assertTrue(
            listOf("mutable_1", "mutable_2", "mutable_3").toTypedArray()
                .contentEquals(activity.mutableArrayListCase.toTypedArray())
        )
        assertEquals(SampleModel("mutable"), activity.mutableSerializableCase)

        // Update condition
        val ldt = LocalDateTime.now()
        val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val updatedString = ldt.format(dateFormat)
        activity.mutableExtraNonNull = updatedString
        activity.mutableExtraNull = updatedString
        activity.mutableArrayListCase = arrayListOf(updatedString)
        activity.mutableSerializableCase = SampleModel(updatedString)

        // Activity recreate
        rule.scenario.recreate()

        // Updated check
        // Check, Non-null
        assertEquals("result1", activity.extraNonNull)
        assertArrayEquals(
            arrayListOf("A", "B", "C").toArray(),
            activity.arrayListCase.toArray()
        )
        assertEquals(SampleModel("A"), activity.serializableCase)

        // Check, Nullable
        assertNull(activity.extraNull)
        assertNull(activity.arrayListNullCase)
        assertNull(activity.serializableNullCase)

        // Check, Mutable
        assertEquals(updatedString, activity.mutableExtraNonNull)
        assertEquals(updatedString, activity.mutableExtraNull)
        assertTrue(
            listOf(updatedString).toTypedArray()
                .contentEquals(activity.mutableArrayListCase.toTypedArray())
        )
        assertEquals(SampleModel(updatedString), activity.mutableSerializableCase)
    }
}

