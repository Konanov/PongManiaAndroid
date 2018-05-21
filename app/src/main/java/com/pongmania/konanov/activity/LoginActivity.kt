package com.pongmania.konanov.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.firebase.auth.FirebaseAuth
import com.pongmania.konanov.PongMania
import com.pongmania.konanov.R
import com.pongmania.konanov.api.PlayerApi
import com.pongmania.konanov.util.DataHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
@SuppressWarnings("unused")
class LoginActivity : AppCompatActivity() {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private val TAG = "LoginActivity"

    private lateinit var email: String
    private lateinit var password: String

    @BindView(R.id.login_email) lateinit var loginEmail: EditText
    @BindView(R.id.et_password) lateinit var etPassword: EditText
    private lateinit var mProgressBar: ProgressBar

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var mAuth: FirebaseAuth

    @OnClick(R.id.btn_login)
    fun login() {
        loginUser()
    }

    @OnClick(R.id.tv_forgot_password)
    fun forgotPassword() {
        startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
    }

    @OnClick(R.id.btn_register_account)
    fun registerAccount() {
        startActivity(Intent(this@LoginActivity, CreateAccountActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as PongMania).webComponent.inject(this)

        mProgressBar = ProgressBar(this)
        mAuth = FirebaseAuth.getInstance()

        if (noPreferences()) {
            setContentView(R.layout.activity_login)
            ButterKnife.bind(this)
        } else {
            email = DataHolder.getEmail(this.application)
            password = DataHolder.getPassword(this.application)
            trySignIn()
        }
    }

    private fun loginUser() {
        email = loginEmail.text.toString()
        password = etPassword.text.toString()
            if (mandatoryFieldsPresent()) {
                DataHolder.setCredentials(this.application, email, password)
                mProgressBar.visibility = View.VISIBLE
                Log.d(TAG, "Logging in user.")
                trySignIn()
            } else {
                Toast.makeText(this, "Заполните все поля формы", Toast.LENGTH_SHORT).show()
            }
    }

    private fun trySignIn() {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    mProgressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Sign in success, update UI with signed-in user information
                        Log.d(TAG, "signInWithEmail:success")
                        chooseNextActivity()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun mandatoryFieldsPresent() = !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)

    private fun noPreferences() = DataHolder.getEmail(this.application).isEmpty() ||
            DataHolder.getPassword(this.application).isEmpty()

    private fun chooseNextActivity() {
        retrofit.create(PlayerApi::class.java).playerHasLeague(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                        if (!result) {
                            goFor(this@LoginActivity, AssignLeagueActivity::class.java)
                        } else {
                            goFor(this@LoginActivity, MainActivity::class.java)
                        }
                })
    }
}