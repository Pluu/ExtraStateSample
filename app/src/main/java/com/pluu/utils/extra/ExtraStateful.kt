package com.pluu.utils.extra

import android.os.Bundle
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface ExtraStateful {

    /**
     * The following methods register properties to be restored / saved during calls
     * to restore() / state()
     */
    fun <T : Any> extra(): PropertyDelegateProvider<ExtraStateful, ReadWriteProperty<ExtraStateful, T>>

    fun <T : Any?> extraNullable(): PropertyDelegateProvider<ExtraStateful, ReadWriteProperty<ExtraStateful, T?>>

    /**
     * Set all registered properties to values from the input bundle
     */
    fun restore(state: Bundle?, savedInstanceState: Bundle?)

    /**
     * Get all registered property values and put them on the input bundle
     */
    fun save(state: Bundle)
}

fun extraStateful(): ExtraStateful = object : ExtraStateful {
    private val consumeKey = mutableSetOf<String>()
    private val bundle = Bundle()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> extra(): PropertyDelegateProvider<Any, ReadWriteProperty<Any, T>> =
        PropertyDelegateProvider { _, property ->
            consumeKey.add(property.name)
            ExtraDelegate(
                getter = { key ->
                    checkNotNull(bundle.get(key) as T) {
                        "Property $key could not be read"
                    }
                },
                setter = { key, value ->
                    bundle.put(key, value)
                }
            )
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> extraNullable(): PropertyDelegateProvider<Any, ReadWriteProperty<ExtraStateful, T?>> =
        PropertyDelegateProvider { _, property ->
            consumeKey.add(property.name)
            ExtraDelegate(
                getter = { key ->
                    bundle.get(key) as? T
                },
                setter = { key, value ->
                    bundle.put(key, value)
                }
            )
        }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun restore(defaultState: Bundle?, restoredState: Bundle?) {
        bundle.clear()
        if (defaultState != null) {
            consumeKey.forEach { key ->
                if (defaultState.containsKey(key)) {
                    bundle.put(key, defaultState.get(key))
                }
            }
        }
        if (restoredState != null) {
            consumeKey.forEach { key ->
                if (restoredState.containsKey(key)) {
                    bundle.put(key, restoredState.get(key))
                }
            }
        }
    }

    override fun save(state: Bundle) {
        consumeKey.forEach { key ->
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