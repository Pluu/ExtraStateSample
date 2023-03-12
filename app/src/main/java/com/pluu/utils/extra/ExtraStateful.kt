package com.pluu.utils.extra

import android.os.Bundle
import com.pluu.utils.extra.ExtraStateful.Companion.KEY_EXTRA_STATEFUL
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface ExtraStateful {

    /**
     * The following methods register properties to be restored / saved during calls
     * to restore() / state()
     */
    fun <T : Any> extra(): ReadWriteProperty<ExtraStateful, T>

    fun <T : Any?> extraNullable(): ReadWriteProperty<ExtraStateful, T?>

    /**
     * Set all registered properties to values from the input bundle
     */
    fun restore(state: Bundle?, savedInstanceState: Bundle?)

    /**
     * Get all registered property values and put them on the input bundle
     */
    fun save(state: Bundle)

    companion object {
        const val KEY_EXTRA_STATEFUL = "KEY_EXTRA_STATEFUL"
    }
}

fun extraStateful(): ExtraStateful = object : ExtraStateful {
    private val restoreKey = mutableSetOf<String>()
    private val bundle = Bundle()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> extra(): ReadWriteProperty<Any, T> {
        return ExtraDelegate(
            getter = { key ->
                checkNotNull(bundle.get(key) as T) {
                    "Property $key could not be read"
                }
            },
            setter = { key, value ->
                restoreKey.add(key)
                bundle.put(key, value)
            }
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> extraNullable(): ReadWriteProperty<ExtraStateful, T?> {
        return ExtraDelegate(
            getter = { key ->
                bundle.get(key) as? T
            },
            setter = { key, value ->
                restoreKey.add(key)
                bundle.put(key, value)
            }
        )
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun restore(defaultState: Bundle?, restoredState: Bundle?) {
        bundle.clear()
        if (defaultState != null) {
            bundle.putAll(defaultState)
        }
        if (restoredState != null) {
            restoreKey.addAll(restoredState.getStringArray(KEY_EXTRA_STATEFUL).orEmpty())
            bundle.putAll(restoredState)
        }
    }

    override fun save(state: Bundle) {
        state.putStringArray(KEY_EXTRA_STATEFUL, restoreKey.toTypedArray())
        restoreKey.forEach { key ->
            state.put(key, bundle.get(key))
        }
    }

    private inner class ExtraDelegate<T>(
        private val getter: (String) -> T,
        private val setter: (String, T) -> Unit
    ) : ReadWriteProperty<Any, T> {
        @Suppress("UNCHECKED_CAST")
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ): T = getter(property.name)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: T
        ) {
            setter(property.name, value)
        }
    }
}