# ExtraStateSample

Delegation to restore access and state to bundles passed to Activities and Fragments from properties

## Setup

### ExtraStateful

```kotlin
interface ExtraStateful {
  fun <T : Any> extra(): PropertyDelegateProvider<ExtraStateful, ReadWriteProperty<ExtraStateful, T>>

  fun <T : Any?> extraNullable(): PropertyDelegateProvider<ExtraStateful, ReadWriteProperty<ExtraStateful, T?>>

  fun restore(state: Bundle?, savedInstanceState: Bundle?)

  fun save(state: Bundle)
}
```

Provides a default implementor extraStateful that implements the ExtraStateful interface.

### Activity Setup

```kotlin
class ActivityStateActivity : AppCompatActivity(), ExtraStateful by extraStateful() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    restore(intent?.extras, savedInstanceState) // (Required call) Load/Restore extra property
    ...
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    save(outState) // (Required call) Save extra property
  }
}
```

### Fragment Setup

```kotlin
class FragmentStateFragment : Fragment(), ExtraStateful by extraStateful() {  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    restore(arguments, savedInstanceState)  // (Required call) Load/Restore extra property
  }
  
  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    save(outState) // (Required call) Save extra property
  }
}
```

## Use Delegation-property

- extra : NonNull extra delegate
- extraNullable : Optional extra delegate

### Use in activity

```kotlin
class ActivityStateActivity : AppCompatActivity(), ExtraStateful by extraStateful() {
  private val extraNonNull by extra<TYPE>()

  private val extraNull by extraNullable<TYPE>()
}
```
