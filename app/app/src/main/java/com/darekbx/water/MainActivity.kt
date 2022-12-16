@file:OptIn(ExperimentalPermissionsApi::class)

package com.darekbx.water

import android.Manifest.permission.*
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.darekbx.communication.BluetoothService
import com.darekbx.water.ui.theme.WaterTheme
import com.darekbx.water.ui.waterlevel.viewmodel.WaterLevelViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * Water
 *
 * white screen with water level, water is waving on the top like in a glass.
 * With mesurement scale. Day's goal is to drink 2L (can be configured).
 * In the center how many water was drinked during day.
 * Random bubbles in water, they will pop and dissapear.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val serviceIntent by lazy { Intent(this, BluetoothService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WaterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val permissionState = rememberPermissionState(permission = ACCESS_FINE_LOCATION)

                    if (permissionState.status.isGranted) {
                        MainScreen(
                            onStart = { startForegroundService(serviceIntent) },
                            onStop = { stopService(serviceIntent) }
                        )
                    } else {
                        Column {
                            val textToShow = if (permissionState.status.shouldShowRationale) {
                                "The location is important for this app. Please grant the permission."
                            } else {
                                "Location permission required for this feature to be available. " +
                                        "Please grant the permission"
                            }
                            Text(textToShow)
                            Button(onClick = { permissionState.launchPermissionRequest() }) {
                                Text("Request permission")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MainScreen(
    onStart: () -> Unit = { },
    onStop: () -> Unit = { },
    waterLevelViewModel: WaterLevelViewModel = hiltViewModel()
) {

    val status by waterLevelViewModel.status().observeAsState()
    val waterLevels by waterLevelViewModel.waterLevels().observeAsState()

    Column(Modifier.padding(16.dp)) {

        Button(onClick = { onStart() }) {
            Text(text = "Start")
        }

        Button(onClick = { onStop() }) {
            Text(text = "Stop")
        }

        Text(text = "Status: ${status?.value}")
        Text(text = "Data: ${waterLevels?.size}")
    }
}
