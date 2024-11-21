package com.example.criminalintent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import java.util.UUID

private const val TAG = "CrimeDetailViewModel"

class CrimeDetailViewModel(): ViewModel() {
    private val  crimeRepository = CrimeRepository.get()
    private val crimeIdLiveData = MutableLiveData<UUID>()

//    var crimeLiveData: LiveData<Crime?> = Transformations.map(crimeIdLiveData) {
//        crimeId -> crimeRepository.getCrime(crimeId)
//    }

    var crimeLiveData: LiveData<Crime?> = crimeIdLiveData.switchMap{
        crimeId -> crimeRepository.getCrime(crimeId)
    }

    fun loadCrime(crimeId: UUID){
//        if (crimeIdLiveData.value != crimeId){
//            crimeIdLiveData.value = crimeId
//        } else {
//            crimeIdLiveData.value = crimeIdLiveData.value
//        }

//        crimeRepository.getCrime(crimeId).observeForever { crime ->
//            Log.d(TAG, "Crime loaded: $crime")
//            crimeIdLiveData.value = crimeId
//        }

        crimeIdLiveData.value = crimeId
        Log.d(TAG, "ПРЕСТУПЛЕНИЕ В CrimeDetailViewModel $crimeId")
    }

}