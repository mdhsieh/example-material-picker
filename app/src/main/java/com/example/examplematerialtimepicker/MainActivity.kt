package com.example.examplematerialtimepicker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
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
import androidx.fragment.app.FragmentManager
import com.example.examplematerialtimepicker.ui.theme.ExampleMaterialTimePickerTheme
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val TAG = "MainActivity"
private val TIME_FRAGMENT_TAG = "time_picker_frag"
private val DATE_FRAGMENT_TAG = "date_picker_frag"

// Extend AppCompatActivity instead of ComponentActivity.
// AppCompatActivity extends FragmentActivity which extends ComponentActivity.
class MainActivity : AppCompatActivity() {
    private fun AppCompatActivity.getCurrentFragmentManager(): FragmentManager {
        return supportFragmentManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExampleMaterialTimePickerTheme {
                AppContent(fragmentManager = getCurrentFragmentManager())
            }
        }
    }
}

@Composable
fun AppContent(fragmentManager: FragmentManager) {
    var showTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedDateTime by remember { mutableStateOf(LocalDateTime.now()) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
            Text("Show Time Picker")
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimePickerCheckbox(
                    showTimePicker = showTimePicker,
                    onShowTimePickerChanged = { newValue ->
                        showTimePicker = newValue
                    }
                )
                Text("Selected Time: ${selectedDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")
            }
            ShowMaterialTimePicker(
                showTimePicker = showTimePicker,
                selectedDateTime = LocalDateTime.now(),
                onTimeSelected = {
                    /* Handle selected time */
                 },
                fragmentManager = fragmentManager
            )


            Text("Show Date Picker")
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                DatePickerCheckbox(
                    showDatePicker = showDatePicker,
                    onShowDatePickerChanged = { newValue ->
                        showDatePicker = newValue
                    }
                )
                Text("Selected Date: $selectedDate")
            }

    }
}

@Composable
fun TimePickerCheckbox(
    showTimePicker: Boolean,
    onShowTimePickerChanged: (Boolean) -> Unit
) {
    Checkbox(
        checked = showTimePicker,
        onCheckedChange = { newValue ->
            onShowTimePickerChanged(newValue)
        },
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun DatePickerCheckbox(
    showDatePicker: Boolean,
    onShowDatePickerChanged: (Boolean) -> Unit
) {
    Checkbox(
        checked = showDatePicker,
        onCheckedChange = { newValue ->
            onShowDatePickerChanged(newValue)
        },
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun ShowMaterialTimePicker(
    showTimePicker: Boolean,
    selectedDateTime: LocalDateTime,
    onTimeSelected: (LocalDateTime) -> Unit,
    fragmentManager: FragmentManager // Pass the supportFragmentManager as a parameter
) {
    if (showTimePicker) {
        val timePickerDialog = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(selectedDateTime.hour)
            .setMinute(selectedDateTime.minute)
            .build()

        timePickerDialog.show(fragmentManager, TIME_FRAGMENT_TAG)
        timePickerDialog.addOnPositiveButtonClickListener {
            val selectedTime = LocalTime.of(timePickerDialog.hour, timePickerDialog.minute)
            val newDateTime = selectedDateTime.with(selectedTime)
            onTimeSelected(newDateTime)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PickerCheckboxPreview() {

    var showTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedDateTime by remember { mutableStateOf(LocalDateTime.now()) }

    ExampleMaterialTimePickerTheme {
        TimePickerCheckbox(
            showTimePicker = showTimePicker,
            onShowTimePickerChanged = { newValue ->
                showTimePicker = newValue
            }
        )
        Text("Selected Time: ${selectedDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")

        DatePickerCheckbox(
            showDatePicker = showDatePicker,
            onShowDatePickerChanged = { newValue ->
                showDatePicker = newValue
            }
        )
        Text("Selected Date: $selectedDate")
    }
}