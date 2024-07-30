package com.learner.listapplication

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class LocationViewModel: ViewModel() {
    private val _location = mutableStateOf<LocationData?>(null)
    val location : State<LocationData?> = _location

    private val _address = mutableStateOf(listOf<GeocodingResult>())
    val address : State<List<GeocodingResult>> = _address

    fun updateLocation(newLocation: LocationData){
        _location.value = newLocation
    }

    fun fetchAddress(latLng: String,
                     context: Context){
        try{
            viewModelScope.launch {
                var result = getApiKey(context)?.let {
                    RetrofitClient
                        .create()
                        .getAddressFromCoordinates(
                            latLng,
                            it
                        )
                }
                if (result != null) {
                    _address.value = result.results
                }
            }
        } catch (ex: Exception){
            Log.d("res1", "${ex.cause} ${ex.message}")
        }
    }

    fun getApiKey(context: Context): String? {
        try {
            val applicationInfo = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val metaData = applicationInfo.metaData
            if (metaData != null) {
                return metaData.getString("com.google.android.geo.API_KEY")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("getApiKey", "Failed to load meta-data, NameNotFound: " + e.message)
        } catch (e: NullPointerException) {
            Log.e("getApiKey", "Failed to load meta-data, NullPointer: " + e.message)
        }
        return null
    }
}