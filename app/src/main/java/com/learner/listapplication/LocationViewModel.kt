package com.learner.listapplication

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class LocationViewModel: ViewModel() {
    private val _location = mutableStateOf<LocationData?>(null)
    val location : State<LocationData?> = _location

    private val _address = mutableStateOf(listOf<GeocodingResult>())
    val address : State<List<GeocodingResult>> = _address

    fun updateLocation(newLocation: LocationData){
        _location.value = newLocation
    }

    fun fetchAddress(latLng: String){
        try{
            viewModelScope.launch {
                var result = RetrofitClient
                    .create()
                    .getAddressFromCoordinates(
                        latLng,
                        "AIzaSyDYaEtgJncZkVrgbLEhRBLSSjpcxhO8YRE"
                    )
                _address.value = result.results
            }
        } catch (ex: Exception){
            Log.d("res1", "${ex.cause} ${ex.message}")
        }
    }
}