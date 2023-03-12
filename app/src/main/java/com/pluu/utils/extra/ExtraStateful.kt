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
    private val propertyKey = mutableSetOf<String>()
    private val extras = mutableMapOf<String, Any?>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> extra(): PropertyDelegateProvider<Any, ReadWriteProperty<Any, T>> =
        PropertyDelegateProvider { _, property ->
            propertyKey.add(property.name)
            ExtraDelegate(
                getter = { key ->
                    checkNotNull(extras[key] as T) {
                        "Property $key could not be read"
                    }
                },
                setter = { key, value ->
                    extras[key] = value
                }
            )
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> extraNullable(): PropertyDelegateProvider<Any, ReadWriteProperty<ExtraStateful, T?>> =
        PropertyDelegateProvider { _, property ->
            propertyKey.add(property.name)
            ExtraDelegate(
                getter = { key ->
                    extras[key] as? T
                },
                setter = { key, value ->
                    extras[key] = value
                }
            )
        }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun restore(defaultState: Bundle?, restoredState: Bundle?) {
        extras.clear()
        if (defaultState != null) {
            propertyKey.forEach { key ->
                if (defaultState.containsKey(key)) {
                    extras[key] = defaultState.get(key)
                }
            }
        }
        if (restoredState != null) {
            propertyKey.forEach { key ->
                if (restoredState.containsKey(key)) {
                    extras[key] = restoredState.get(key)
                }
            }
        }
    }

    override fun save(state: Bundle) {
        propertyKey.forEach { key ->
            state.put(key, extras[key])
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