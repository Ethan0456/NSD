package com.ethan.nsd

import android.content.ContentValues.TAG
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import com.ethan.nsd.Tag.*
import com.ethan.nsd.ui.theme.NSDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NSDTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val ls = MutableLiveData<List<NsdServiceInfo>>(listOf())
                    val liveLs: MutableLiveData<List<NsdServiceInfo>> = ls
                    val obs = liveLs.observeAsState()
                    val nsdHelper = NsdHelper(ls)

                    lifecycle.addObserver(CustomLifeCycleObserver(this, nsdHelper))

                    nsdHelper.initializeServerSocket()
                    nsdHelper.registerService(this)
                    nsdHelper.discoverServices()


                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(obs.value!!.size) {item ->
                            Button(
                                onClick = {
                                    nsdHelper.onClickResolve(obs.value!![item])
                                }
                            ) {
                                Text(text= obs.value!![item].serviceName)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun log(tag: Tag,msg: String) {
    when (tag) {
        DEBUG -> Log.d(TAG, msg)
        INFO -> Log.i(TAG, msg)
        WARNING -> Log.w(TAG, msg)
        ERROR -> Log.e(TAG, msg)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NSDTheme {
        Greeting("Android")
    }
}