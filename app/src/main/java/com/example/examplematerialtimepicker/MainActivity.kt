package com.example.examplematerialtimepicker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.examplematerialtimepicker.ui.theme.ExampleMaterialTimePickerTheme
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDateTime

private val TAG = "MainActivity"
private val FRAGMENT_TAG = "time_picker_frag"

// Extend AppCompatActivity instead of ComponentActivity.
// AppCompatActivity extends FragmentActivity which extends ComponentActivity.
// class MainActivity : ComponentActivity() {
class MainActivity : AppCompatActivity() {

    private val shouldShowTimePicker = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExampleMaterialTimePickerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CreateMaterialTimePickerCheckbox(shouldShowTimePicker)
                    if (shouldShowTimePicker.value) {
                        CreateMaterialTimePicker(shouldShowTimePicker)
                    }
                }
            }
        }
    }
}

@Composable
fun CreateMaterialTimePickerCheckbox(shouldShowTimePicker: MutableState<Boolean>) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
        Checkbox(
            modifier = Modifier.scale(scale = 1.6f),
            checked = shouldShowTimePicker.value,
            onCheckedChange = { isChecked ->
                shouldShowTimePicker.value = isChecked
            }
        )

        Text(
            modifier = Modifier.padding(start = 2.dp),
            text = "Select Time"
        )
    }
}

@Composable
fun CreateMaterialTimePicker(shouldShowTimePicker: MutableState<Boolean>) {
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
                addOnCancelListener { shouldShowTimePicker.value = false }
                addOnDismissListener { shouldShowTimePicker.value = false }
                addOnPositiveButtonClickListener {
                    Toast.makeText(
                        context,
                        "Selected time: $hour : $minute",
                        Toast.LENGTH_SHORT
                    ).show()
                    shouldShowTimePicker.value = false
                }
                addOnNegativeButtonClickListener { shouldShowTimePicker.value = false }
            }
            .show(activity.supportFragmentManager, FRAGMENT_TAG)

    } else {
        Log.e(TAG, "Activity is null")
    }
}

@Preview(showBackground = true)
@Composable
fun PickerCheckboxPreview() {
    val shouldShowTimePicker = mutableStateOf(false)

    ExampleMaterialTimePickerTheme {
        CreateMaterialTimePickerCheckbox(shouldShowTimePicker)
    }
}