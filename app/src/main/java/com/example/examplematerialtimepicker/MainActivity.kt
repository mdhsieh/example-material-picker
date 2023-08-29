package com.example.examplematerialtimepicker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.examplematerialtimepicker.ui.theme.ExampleMaterialTimePickerTheme
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

private val TAG = "MainActivity"
private val TIME_FRAGMENT_TAG = "time_picker_frag"
private val DATE_FRAGMENT_TAG = "date_picker_frag"

// Extend AppCompatActivity instead of ComponentActivity.
// AppCompatActivity extends FragmentActivity which extends ComponentActivity.
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var shouldShowTimePicker by remember { mutableStateOf(false) }
            var shouldShowDatePicker by remember { mutableStateOf(false) }
            var selectedDate by remember { mutableStateOf(LocalDate.now()) }

            ExampleMaterialTimePickerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        CreateMaterialTimePickerCheckbox(
                            showTimePicker = shouldShowTimePicker,
                            onCheckboxChanged = { newValue ->
                                shouldShowTimePicker = newValue
                            }
                        )
                        if (shouldShowTimePicker) {
                            CreateMaterialTimePicker(
                                showTimePicker = shouldShowTimePicker,
                                onTimeSelected = { newValue ->
                                    shouldShowTimePicker = newValue
                                }
                            )
                        }
                        CreateMaterialDatePickerCheckbox(
                            showDatePicker = shouldShowDatePicker,
                            onCheckboxChanged = { newValue ->
                                shouldShowDatePicker = newValue
                            }
                        )
                        if (shouldShowDatePicker) {
                            BasicTextField(
                                value = selectedDate.toString(),
                                onValueChange = { /* Handle text input here */ }
                            )

                            CreateMaterialDatePicker(
                                selectedDate = selectedDate,
                                onDateSelected = { date ->
                                    selectedDate = date
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreateMaterialTimePickerCheckbox(
    showTimePicker: Boolean,
    onCheckboxChanged: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
        Checkbox(
            modifier = Modifier.scale(scale = 1.6f),
            checked = showTimePicker,
            onCheckedChange = { isChecked ->
                onCheckboxChanged(isChecked)
            }
        )

        Text(
            modifier = Modifier.padding(start = 2.dp),
            text = "Select Time"
        )
    }
}

@Composable
fun CreateMaterialDatePickerCheckbox(
    showDatePicker: Boolean,
    onCheckboxChanged: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
        Checkbox(
            modifier = Modifier.scale(scale = 1.6f),
            checked = showDatePicker,
            onCheckedChange = { isChecked ->
                onCheckboxChanged(isChecked)
            }
        )

        Text(
            modifier = Modifier.padding(start = 2.dp),
            text = "Select Date"
        )
    }
}

@Composable
fun CreateMaterialTimePicker(showTimePicker: Boolean, onTimeSelected: (Boolean) -> Unit) {
    // Get the current Activity from the Composable
    val context: Context = LocalContext.current
    val activity = context as? AppCompatActivity

    // LocalDateTime requires min API level 26
    val current = LocalDateTime.now()

    if (activity is AppCompatActivity) {
        Log.d(TAG, "Success cast to AppCompatActivity")

        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(current.hour)
            .setMinute(current.minute)
            .setTitleText("Time of the meeting")
            .build().apply {
                addOnCancelListener { onTimeSelected(false) }
                addOnDismissListener { onTimeSelected(false) }
                addOnPositiveButtonClickListener {
                    Toast.makeText(
                        context,
                        "Selected time: $hour : $minute",
                        Toast.LENGTH_SHORT
                    ).show()
                    onTimeSelected(true)
                }
                addOnNegativeButtonClickListener { onTimeSelected(false) }
            }
            .show(activity.supportFragmentManager, TIME_FRAGMENT_TAG)

    } else {
        Log.e(TAG, "Activity is null")
    }
}

@Composable
fun CreateMaterialDatePicker(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val context: Context = LocalContext.current
    val activity = context as? AppCompatActivity

    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select Date")
        // Convert to milliseconds
        .setSelection(selectedDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000)
        .build()

    Button(
        onClick = {
            if (activity != null) {
                datePicker.showNow(activity.supportFragmentManager, DATE_FRAGMENT_TAG)
            }
            datePicker.addOnPositiveButtonClickListener {
                val selectedMillis = datePicker.selection ?: 0
                val newSelectedDate = LocalDate.ofEpochDay(selectedMillis / 1000 / 60 / 60 / 24)
                onDateSelected(newSelectedDate)
            }
        }
    ) {
        Text("Open Date Picker")
    }
}

@Preview(showBackground = true)
@Composable
fun PickerCheckboxPreview() {
    var shouldShowTimePicker by remember { mutableStateOf(false) }
    var shouldShowDatePicker by remember { mutableStateOf(false) }

    ExampleMaterialTimePickerTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            CreateMaterialTimePickerCheckbox(shouldShowTimePicker, onCheckboxChanged = { newValue ->
                shouldShowTimePicker = newValue
            })
            CreateMaterialDatePickerCheckbox(shouldShowDatePicker, onCheckboxChanged = { newValue ->
                shouldShowDatePicker = newValue
            })
        }
    }
}