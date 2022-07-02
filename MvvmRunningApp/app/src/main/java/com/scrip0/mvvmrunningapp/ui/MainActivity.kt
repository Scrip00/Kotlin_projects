package com.scrip0.mvvmrunningapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scrip0.mvvmrunningapp.R
import com.scrip0.mvvmrunningapp.db.RunDAO
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	@Inject
	lateinit var runDAO: RunDAO

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

	}
}