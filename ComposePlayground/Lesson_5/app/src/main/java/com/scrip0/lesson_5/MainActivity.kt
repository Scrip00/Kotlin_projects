package com.scrip0.lesson_5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			val scrollState = rememberScrollState()
			Row {
				Column(
					modifier = Modifier
						.verticalScroll(scrollState)
						.weight(1f)
				) {
					for (i in 1..50) {
						Text(
							text = "Item $i",
							fontSize = 24.sp,
							fontWeight = FontWeight.Bold,
							textAlign = TextAlign.Center,
							modifier = Modifier
								.fillMaxWidth()
								.padding(vertical = 24.dp)
						)
					}
				}
				LazyColumn(modifier = Modifier.weight(1f)) {
					itemsIndexed(
						listOf(
							"This",
							"is",
							"jetpack",
							"compose",
							"This",
							"is",
							"jetpack",
							"compose",
							"This",
							"is",
							"jetpack",
							"compose",
							"This",
							"is",
							"jetpack",
							"compose",
							"This",
							"is",
							"jetpack",
							"compose",
							"This",
							"is",
							"jetpack",
							"compose",
							"This",
							"is",
							"jetpack",
							"compose",
							"This",
							"is",
							"jetpack",
							"compose",
							"This",
							"is",
							"jetpack",
							"compose"
						)
					) { index, string ->
						Text(
							text = "$string - $index",
							fontSize = 24.sp,
							fontWeight = FontWeight.Bold,
							textAlign = TextAlign.Center,
							modifier = Modifier
								.fillMaxWidth()
								.padding(vertical = 24.dp)
						)
					}
//					items(5000) {
//						Text(
//							text = "Item $it",
//							fontSize = 24.sp,
//							fontWeight = FontWeight.Bold,
//							textAlign = TextAlign.Center,
//							modifier = Modifier
//								.fillMaxWidth()
//								.padding(vertical = 24.dp)
//						)
//					}
				}
			}
		}
	}
}