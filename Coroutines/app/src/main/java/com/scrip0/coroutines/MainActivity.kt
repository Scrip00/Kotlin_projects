package com.scrip0.coroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

	val TAG = "MainActivity"

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		GlobalScope.launch {
			val networkCallAnswer = doNetworkCall()
			val networkCallAnswer2 = doNetworkCall2()
			Log.d(TAG, networkCallAnswer)
			Log.d(TAG, networkCallAnswer2)
			Log.d(TAG, "Coroutine says hello from thread ${Thread.currentThread().name}")
		}
		Log.d(TAG, "Hello from thread here ${Thread.currentThread().name}")
	}

	private suspend fun doNetworkCall(): String {
		delay(3000L)
		return "This is custom suspend f-n"
	}

	private suspend fun doNetworkCall2(): String {
		delay(3000L)
		return "This is custom suspend f-n"
	}
}