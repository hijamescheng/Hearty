package au.com.hearty.extension

import androidx.lifecycle.MutableLiveData

fun <T : Any?> MutableLiveData<T>.refresh() {
    this.value = this.value
}