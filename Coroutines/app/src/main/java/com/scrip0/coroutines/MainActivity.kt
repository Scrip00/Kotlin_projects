package com.scrip0.coroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

	val TAG = "MainActivity"

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		GlobalScope.launch(Dispatchers.IO) {
			Log.d(TAG, "Starting coroutine in thread ${Thread.currentThread().name}")
			val networkCallAnswer = doNetworkCall()
			withContext(Dispatchers.Main) {
				tvDummy.text = networkCallAnswer
				Log.d(TAG, "Ending up in thread ${Thread.currentThread().name}")
			}
		}
	}

	private suspend fun doNetworkCall(): String {
		delay(3000L)
		return "This is a fake network call"
	}
}