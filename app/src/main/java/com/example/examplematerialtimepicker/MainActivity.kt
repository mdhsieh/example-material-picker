package com.example.examplematerialtimepicker

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.example.examplematerialtimepicker.ui.theme.ExampleMaterialTimePickerTheme
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Instant
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
                selectedDateTime = selectedDateTime,
                onTimeSelected = {
                    selectedDateTime = it
                    showTimePicker = false
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
                Text("Selected Date: ${selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
            }
            ShowMaterialDatePicker(
                showDatePicker = showDatePicker,
                selectedDate = selectedDate,
                onDateSelected = {
                    selectedDate = it
                    showDatePicker = false
                },
                fragmentManager = fragmentManager
            )

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

@Composable
fun ShowMaterialDatePicker(
    showDatePicker: Boolean,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    fragmentManager: FragmentManager
) {
    if (showDatePicker) {

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(selectedDate.toEpochDay() * 24 * 60 * 60 * 1000)
            .setTitleText("Select a Date")
            .build()

        datePicker.addOnPositiveButtonClickListener { millis ->
            val selectedLocalDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
            onDateSelected(selectedLocalDate)
        }

        datePicker.show(fragmentManager, DATE_FRAGMENT_TAG)
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