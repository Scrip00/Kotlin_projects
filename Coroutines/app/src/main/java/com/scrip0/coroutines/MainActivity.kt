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
			delay(3000L)
			Log.d(TAG, "Coroutine says hello from thread ${Thread.currentThread().name}")
		}
		Log.d(TAG, "Hello from thread here ${Thread.currentThread().name}")
	}
}