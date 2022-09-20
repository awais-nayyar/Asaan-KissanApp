package com.example.asaankissan.activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.asaankissan.R
import com.example.asaankissan.databinding.ActivityGoogleSignupBinding
import com.example.asaankissan.firebase.FirebaseClass
import com.example.asaankissan.fragments.AccountFragment
import com.example.asaankissan.models.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class GoogleSignupActivity : AppCompatActivity() {

    private var binding : ActivityGoogleSignupBinding? = null

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private val RC_SIGN_UP = 142

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleSignupBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        createRequest()

        binding?.btnSignupGoogle?.visibility = View.VISIBLE
        binding?.progressBar?.visibility = View.INVISIBLE

        binding?.btnSignupGoogle?.setOnClickListener{
            signIn()
            binding?.btnSignupGoogle?.visibility = View.INVISIBLE
            binding?.lottie?.visibility = View.INVISIBLE
            binding?.tvWellcome?.visibility = View.INVISIBLE
            binding?.btnHowToUse?.visibility = View.INVISIBLE
            binding?.progressBar?.visibility = View.VISIBLE

        }

        // goto user guide activity
        binding?.btnHowToUse?.setOnClickListener {
            val userGuideIntent = Intent(this@GoogleSignupActivity, UserGuideActivity::class.java)
            startActivity(userGuideIntent)
        }
    }

    private fun createRequest() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_UP)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    firebaseUser = auth.currentUser!!
                    FirebaseClass().findUserByEmail(this, firebaseUser.email!!)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "signin failed", Toast.LENGTH_SHORT).show()
                    val myFireStore = FirebaseFirestore.getInstance()
                }
            }
    }

    fun userAlreadyExistedInDatabase(user: User) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun userNotExistedInDatabase() {
        val user = User(
            id = firebaseUser.uid,
            name = firebaseUser.displayName!!,
            //phone = firebaseUser.phoneNumber!!,
            email = firebaseUser.email!!,
            imageUrl = firebaseUser.photoUrl!!.toString()

        )
        FirebaseClass().saveUserDataToFireStore(this, user)
    }

    fun newUserSavedSuccessfully() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_UP) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data!!)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.d("tag", "Google sign in failed   $e")
                Toast.makeText(this, "signin2 failed", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            //intent.putExtra(Constants.USER_INTENT, currentUser)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        if (binding != null) {
            binding = null
        }
        super.onDestroy()
    }
}