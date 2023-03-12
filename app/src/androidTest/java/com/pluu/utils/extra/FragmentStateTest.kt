package com.pluu.utils.extra

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pluu.savedstate.sample.model.SampleModel
import com.pluu.utils.extra.test.ExtraTestFragment
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RunWith(AndroidJUnit4::class)
class FragmentStateTest {
    private val fragmentArgs = bundleOf(
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

    @Test
    fun t1() {
        val scenario = launchFragmentInContainer<ExtraTestFragment>(fragmentArgs)

        lateinit var fragment: ExtraTestFragment
        scenario.onFragment {
            fragment = it
        }

        // Pre-condition check
        // Check, Non-null
        assertEquals("result1", fragment.extraNonNull)
        assertArrayEquals(
            arrayListOf("A", "B", "C").toArray(),
            fragment.arrayListCase.toArray()
        )
        assertEquals(SampleModel("A"), fragment.serializableCase)

        // Check, Nullable
        assertNull(fragment.extraNull)
        assertNull(fragment.arrayListNullCase)
        assertNull(fragment.serializableNullCase)

        // Check, Mutable
        assertEquals("mutable_result1", fragment.mutableExtraNonNull)
        assertNull(fragment.mutableExtraNull)
        assertTrue(
            listOf("mutable_1", "mutable_2", "mutable_3").toTypedArray()
                .contentEquals(fragment.mutableArrayListCase.toTypedArray())
        )
        assertEquals(SampleModel("mutable"), fragment.mutableSerializableCase)

        // Update condition
        val ldt = LocalDateTime.now()
        val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val updatedString = ldt.format(dateFormat)
        fragment.mutableExtraNonNull = updatedString
        fragment.mutableExtraNull = updatedString
        fragment.mutableArrayListCase = arrayListOf(updatedString)
        fragment.mutableSerializableCase = SampleModel(updatedString)

        // Activity recreate
        scenario.recreate()

        // Updated check
        // Check, Non-null
        assertEquals("result1", fragment.extraNonNull)
        assertArrayEquals(
            arrayListOf("A", "B", "C").toArray(),
            fragment.arrayListCase.toArray()
        )
        assertEquals(SampleModel("A"), fragment.serializableCase)

        // Check, Nullable
        assertNull(fragment.extraNull)
        assertNull(fragment.arrayListNullCase)
        assertNull(fragment.serializableNullCase)

        // Check, Mutable
        assertEquals(updatedString, fragment.mutableExtraNonNull)
        assertEquals(updatedString, fragment.mutableExtraNull)
        assertTrue(
            listOf(updatedString).toTypedArray()
                .contentEquals(fragment.mutableArrayListCase.toTypedArray())
        )
        assertEquals(SampleModel(updatedString), fragment.mutableSerializableCase)
    }
}