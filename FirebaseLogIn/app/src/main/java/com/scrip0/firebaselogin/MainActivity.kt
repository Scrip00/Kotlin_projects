package com.scrip0.firebaselogin

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

	private lateinit var auth: FirebaseAuth

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		auth = FirebaseAuth.getInstance()

		btnRegister.setOnClickListener {
			registerUser()
		}

		btnLogin.setOnClickListener {
			loginUser()
		}

		btnUpdateProfile.setOnClickListener {
			updateProfile()
		}

		btnLogout.setOnClickListener {
			auth.signOut()
			checkLoggedInState()
		}

		ivProfilePicture.setOnClickListener {
			getImgFromGallery.launch("image/*")
		}

		btnSignInGoogle.setOnClickListener {
			val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.webclient_id))
				.requestEmail()
				.requestProfile()
				.build()

			val signInClient = GoogleSignIn.getClient(this, options)
			signInClient.signInIntent.also {
				getGoogleSignIn.launch(it)
			}
		}
	}

	private fun googleAuthForFirebase(account: GoogleSignInAccount) {
		val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
		CoroutineScope(Dispatchers.IO).launch {
			try {
				auth.signInWithCredential(credentials).await()
				withContext(Dispatchers.Main) {
					checkLoggedInState()
					Toast.makeText(
						this@MainActivity,
						"Successfully logged in with Google",
						Toast.LENGTH_LONG
					).show()
				}
			} catch (e: Exception) {
				withContext(Dispatchers.Main) {
					Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
				}
			}
		}
	}

	private val getGoogleSignIn =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
			if (it.resultCode == Activity.RESULT_OK) {
				val account = GoogleSignIn.getSignedInAccountFromIntent(it.data).result
				account?.let { signInAccount ->
					googleAuthForFirebase(signInAccount)
				}
			}
		}

	private val getImgFromGallery =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				updateProfile(uri)
			}
		}

	private fun updateProfile(uri: Uri? = null) {
		auth.currentUser?.let { user ->
			val username = etUsername.text.toString()
			val photoURI = uri ?: user.photoUrl
			val profileUpdates = UserProfileChangeRequest.Builder()
				.setDisplayName(username)
				.setPhotoUri(photoURI)
				.build()

			CoroutineScope(Dispatchers.IO).launch {
				try {
					user.updateProfile(profileUpdates).await()
					withContext(Dispatchers.Main) {
						checkLoggedInState()
						Toast.makeText(
							this@MainActivity,
							"Successfully updated user profile",
							Toast.LENGTH_LONG
						).show()
					}
				} catch (e: Exception) {
					withContext(Dispatchers.Main) {
						Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
					}
				}
			}
		}
	}

	override fun onStart() {
		super.onStart()
		checkLoggedInState()
	}

	private fun registerUser() {
		val email = etEmailRegister.text.toString()
		val password = etPasswordRegister.text.toString()
		if (email.isNotEmpty() && password.isNotEmpty()) {
			CoroutineScope(Dispatchers.IO).launch {
				try {
					auth.createUserWithEmailAndPassword(email, password).await()
					withContext(Dispatchers.Main) {
						checkLoggedInState()
					}
				} catch (e: Exception) {
					withContext(Dispatchers.Main) {
						Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
					}
				}
			}
		}
	}

	private fun loginUser() {
		val email = etEmailLogin.text.toString()
		val password = etPasswordLogin.text.toString()
		if (email.isNotEmpty() && password.isNotEmpty()) {
			CoroutineScope(Dispatchers.IO).launch {
				try {
					auth.signInWithEmailAndPassword(email, password).await()
					withContext(Dispatchers.Main) {
						checkLoggedInState()
					}
				} catch (e: Exception) {
					withContext(Dispatchers.Main) {
						Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
					}
				}
			}
		}
	}

	@SuppressLint("SetTextI18n")
	private fun checkLoggedInState() {
		val user = auth.currentUser
		if (user == null) {
			tvLoggedIn.text = "You are not logged in"
			etUsername.setText("")
			ivProfilePicture.setImageURI(null)
		} else {
			tvLoggedIn.text = "You are logged in"
			etUsername.setText(user.displayName)
			ivProfilePicture.setImageURI(user.photoUrl)
		}
	}
}