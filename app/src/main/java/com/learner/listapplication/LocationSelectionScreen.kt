package com.learner.listapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationSelectionScreen(
    locationData: LocationData,
    onLocationSelected: (LocationData) -> Unit
){

    val userLocation = remember {
        mutableStateOf(LatLng(locationData.latitude, locationData.longitude))
    }

    var cameraPosition = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(userLocation.value, 10f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier
                .weight(1f)
                .padding(top = (16.dp)),
            cameraPositionState =cameraPosition,
            onMapClick = {
                userLocation.value = it
            }
        ){
            Marker(state = MarkerState(position = userLocation.value))
        }
        var newLocationData : LocationData
        Button(
            onClick = {
                newLocationData = LocationData(userLocation.value.latitude, userLocation.value.longitude)
                onLocationSelected(newLocationData)

            }) {
                Text(text = "Set location")
            }
    }
}