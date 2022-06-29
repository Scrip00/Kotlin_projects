package com.scrip0.firebasefirestore

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

	private val personCollectionRef = Firebase.firestore.collection("persons")

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		btnUploadData.setOnClickListener {
			val firstName = etFirstName.text.toString()
			val lastName = etLastName.text.toString()
			val age = etAge.text.toString().toInt()
			val person = Person(firstName, lastName, age)
			savePerson(person)
		}

//		subscribeToReaTimeUpdates()

		btnRetrieveData.setOnClickListener {
			retrievePersons()
		}
	}

	private fun subscribeToReaTimeUpdates() {
		personCollectionRef.addSnapshotListener { value, error ->
			error?.let {
				Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
				return@addSnapshotListener
			}
			value?.let {
				val sb = StringBuilder()
				for (doc in it) {
					val person = doc.toObject<Person>()
					sb.append("$person\n")
				}
				tvPersons.text = sb.toString()
			}
		}
	}

	private fun retrievePersons() = CoroutineScope(Dispatchers.IO).launch {
		val fromAge = etFrom.text.toString().toInt()
		val toAge = etTo.text.toString().toInt()
		try {
			val querySnapshot = personCollectionRef
				.whereGreaterThan("age", fromAge)
				.whereLessThan("age", toAge)
				.orderBy("age")
				.get()
				.await()
				
			val sb = StringBuilder()
			for (doc in querySnapshot.documents) {
				val person = doc.toObject<Person>()
				sb.append("$person\n")
			}
			withContext(Dispatchers.Main) {
				tvPersons.text = sb.toString()
			}
		} catch (e: Exception) {
			withContext(Dispatchers.Main) {
				Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
			}
		}
	}

	private fun savePerson(person: Person) = CoroutineScope(Dispatchers.IO).launch {
		try {
			personCollectionRef.add(person).await()
			withContext(Dispatchers.Main) {
				Toast.makeText(this@MainActivity, "Successfully saved data", Toast.LENGTH_LONG)
					.show()
			}
		} catch (e: Exception) {
			withContext(Dispatchers.Main) {
				Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
			}
		}
	}
}