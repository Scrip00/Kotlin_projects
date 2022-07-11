package com.scrip0.lesson_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			Column(
				modifier = Modifier
//					.width(300.dp)
					.fillMaxHeight(0.5f)
					.fillMaxWidth()
					.background(Color.Green)
//					.padding(top = 50.dp)
//					.requiredWidth(600.dp) // Fixed width
					.border(5.dp, Color.Magenta)
					.padding(5.dp)
					.border(5.dp, Color.Blue)
					.padding(5.dp)
					.border(10.dp, Color.Red)
					.padding(10.dp) // Paddings applied sequentially
			) {
				Card() {
					
				}
				Text(
					"Hello",
					modifier = Modifier.clickable {
						// Do smth
					}
//						.offset(0.dp, 20.dp)
//						.border(5.dp, Color.Yellow)
//						.padding(5.dp)
//						.offset(20.dp, 20.dp)
//						.border(10.dp, Color.Black)
//						.padding(10.dp)
				)
				Spacer(modifier = Modifier.height(50.dp))
				Text("World")
			}
		}
	}
}