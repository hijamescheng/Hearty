package au.com.hearty.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class Coroutines {

    companion object {
        @JvmStatic
        fun <T : Any> io(work: suspend (() -> T?)): Job =
            CoroutineScope(Dispatchers.IO).launch {
                work()
            }

        @JvmStatic
        fun <T : Any> ioThenMain(
            work: suspend (() -> T?),
            errorCallback: ((Exception) -> Unit),
            successCallback: ((T?) -> Unit)? = null
        ): Job =
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val data = CoroutineScope(Dispatchers.IO).async {
                        return@async work()
                    }.await()
                    successCallback?.let {
                        it(data)
                    }
                } catch (e: Exception) {
                    errorCallback?.let {
                        it(e)
                    }
                }
            }
    }
}