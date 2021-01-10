package at.fhj.msd.stopwatch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun<T> MutableLiveData<T>.notifyObservers(){
    this.value = this.value
}