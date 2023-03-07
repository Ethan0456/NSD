package com.ethan.nsd

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class CustomLifeCycleObserver(val context: Context, val nsdHelper: NsdHelper): DefaultLifecycleObserver{
    var isPaused = false
    override fun onPause(owner: LifecycleOwner) {
        log(Tag.INFO, "IN PAUSE")
        nsdHelper?.tearDown()
        isPaused = true
    }

    override fun onResume(owner: LifecycleOwner) {
        log(Tag.INFO, "IN RESUME")
        if (isPaused) {
            nsdHelper?.apply {
                registerService(context)
                discoverServices()
            }
        }
        isPaused = false
    }

    override fun onDestroy(owner: LifecycleOwner) {
        nsdHelper?.tearDown()
    }
}
