package com.scrip0.coroutines

import android.annotation.SuppressLint
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

		calcFib()
	}

	@SuppressLint("SetTextI18n")
	private fun calcFib() {
		val job = GlobalScope.launch(Dispatchers.Default) {
			Log.d(TAG, "Starting long-running calc")
			withTimeout(3000L) {
				for (i in 40..50) {
					if (isActive) {
						val text = "$i for ${fib(i)}"
						Log.d(TAG, text)
					}
				}
			}
			Log.d(TAG, "Ending long-running calc")
		}

//		runBlocking {
//			delay(2000L)
//			job.cancel()
//			Log.d(TAG, "Cancelled job")
//		}
	}

	private fun fib(n: Int): Long {
		return when (n) {
			0 -> 0
			1 -> 1
			else -> fib(n - 1) + fib(n - 2)
		}
	}

	private fun doCoroutineStaff() {
		Log.d(TAG, "Before runBlocking")
		runBlocking {
			launch(Dispatchers.IO) {
				delay(3000L)
				Log.d(TAG, "Finished IO coroutine 1")
			}
			launch(Dispatchers.IO) {
				delay(3000L)
				Log.d(TAG, "Finished IO coroutine 2")
			}
			Log.d(TAG, "Start runBlocking")
			delay(3000L) // Analog of Thread.sleep()
			Log.d(TAG, "End runBlocking")
		}
		Log.d(TAG, "After runBlocking")

		val job = GlobalScope.launch(Dispatchers.Default) {
			Log.d(TAG, "Starting coroutine in thread ${Thread.currentThread().name}")
			repeat(5) {
				val networkCallAnswer = doNetworkCall()
				Log.d(TAG, "Coroutine is still working")
				withContext(Dispatchers.Main) {
					tvDummy.text = networkCallAnswer
					Log.d(TAG, "Ending up in thread ${Thread.currentThread().name}")
				}
			}
		}

		GlobalScope.launch(Dispatchers.Default) {

//			delay(2000L)
//			job.cancel()

//			job.join() // Blocks all other threads till coroutine is finished its job
			Log.d(TAG, "Main thread is continuing")
		}
	}

	private suspend fun doNetworkCall(): String {
		delay(1000L)
		return "This is a fake network call"
	}
}