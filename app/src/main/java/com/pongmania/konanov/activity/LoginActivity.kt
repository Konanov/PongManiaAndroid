package com.pongmania.konanov.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.pongmania.konanov.PongMania
import com.pongmania.konanov.R
import com.pongmania.konanov.api.PongManiaApi
import com.pongmania.konanov.util.CredentialsPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private val TAG = "LoginActivity"

    //global variables
    private var email: String? = null
    private var password: String? = null

    //UI elements
    private var tvForgotPassword: TextView? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnLogin: Button? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressDialog? = null

    //retrofit
    @Inject
    lateinit var retrofit: Retrofit

    //FireBase references
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as PongMania).webComponent.inject(this)
        if (noPreferences()) {
            setContentView(R.layout.activity_login)
            initialise()
        } else {
            updateUI()
            this.finish()
        }
    }

    private fun initialise() {
        tvForgotPassword = findViewById<View>(R.id.tv_forgot_password) as TextView
        etEmail = findViewById<View>(R.id.et_email) as EditText
        etPassword = findViewById<View>(R.id.et_password) as EditText
        btnLogin = findViewById<View>(R.id.btn_login) as Button
        btnCreateAccount = findViewById<View>(R.id.btn_register_account) as Button
        mProgressBar = ProgressDialog(this)

        mAuth = FirebaseAuth.getInstance()

        tvForgotPassword!!
                .setOnClickListener { startActivity(Intent(this@LoginActivity,
                        LoginActivity::class.java)) }

        btnCreateAccount!!
                .setOnClickListener { startActivity(Intent(this@LoginActivity,
                        CreateAccountActivity::class.java)) }

        btnLogin!!.setOnClickListener { loginUser() }
    }


    private fun loginUser() {

        email = etEmail?.text.toString()
        password = etPassword?.text.toString()

            if (mandatoryFieldsPresent()) {
                showProgressBar()
                Log.d(TAG, "Logging in user.")
                trySignIn()
            } else {
                Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
            }
        }

    private fun trySignIn() {
        mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->

                    mProgressBar!!.hide()

                    if (task.isSuccessful) {
                        // Sign in success, update UI with signed-in user information
                        Log.d(TAG, "signInWithEmail:success")
                        updateUI()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun showProgressBar() {
        mProgressBar!!.setMessage("Registering User...")
        mProgressBar!!.show()
    }

    private fun mandatoryFieldsPresent() = !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)

    private fun noPreferences() = CredentialsPreference.getEmail(this.application).isEmpty() ||
            CredentialsPreference.getPassword(this.application).isEmpty()

    private fun updateUI() {
        retrofit.create(PongManiaApi::class.java)
                .countLeaguePlayers(CredentialsPreference.getEmail(this.application))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    run {

                        if (result.compareTo(0) == 0) {
                            val intent = Intent(this@LoginActivity, AssignLeagueActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@LoginActivity, ScoreBoardActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                })
    }
}
