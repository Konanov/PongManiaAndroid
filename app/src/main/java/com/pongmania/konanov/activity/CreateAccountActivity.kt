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
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pongmania.konanov.PongMania
import com.pongmania.konanov.R
import com.pongmania.konanov.api.PongManiaApi
import com.pongmania.konanov.model.Player
import com.pongmania.konanov.util.CredentialsPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Retrofit
import javax.inject.Inject


class CreateAccountActivity: AppCompatActivity() {

    //UI elements
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressDialog? = null

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    private val TAG = "CreateAccountActivity"

    //global variables
    private var firstName: String? = null
    private var lastName: String? = null
    private var email: String? = null
    private var password: String? = null

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        (application as PongMania).webComponent.inject(this)

        initialise()
    }

    private fun initialise() {
        initialiseUI()
        initialiseFireBase()
        btnCreateAccount!!.setOnClickListener { createNewAccount() }
    }

    private fun createNewAccount() {
        extractMandatoryFields()
        if (noMandatoryFieldsEmpty()) {
            mAuth!!
                    .createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this) { task ->
                        mProgressBar!!.hide()

                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")

                            val userId = mAuth!!.currentUser!!.uid
                            verifyEmail()
                            updateUserProfileInformation(userId)
                            tryCreateUser()
                            CredentialsPreference.setCredentials(this.application, email!!, password!!)

                            startLoginActivity()
                        } else {
                            errorWhileCreatingUser(task)
                        }
                    }

        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun errorWhileCreatingUser(task: Task<AuthResult>) {
        Log.w(TAG, "createUserWithEmail:failure", task.exception)
        Toast.makeText(this@CreateAccountActivity, "Ошибка при регистрации пользователя.",
                Toast.LENGTH_SHORT).show()
    }

    private fun tryCreateUser() {
        retrofit.create(PongManiaApi::class.java)
                .createUser(Player.Credentials(email!!, firstName!!, lastName!!, password!!))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { result -> userCreated(result) },
                        { error -> errorWhileCreatingUser(error) }
                )
    }

    private fun errorWhileCreatingUser(error: Throwable) {
        Log.d(TAG, "Request resulted in error\n${error.printStackTrace()}")
        Toast.makeText(this, "Ошибка при создании пользователя",
                Toast.LENGTH_SHORT).show()
    }

    private fun userCreated(result: ResponseBody) {
        Log.d(TAG, "User created\n${result.string()}.")
        Toast.makeText(this, "Пользователь зарегистрирован",
                Toast.LENGTH_SHORT).show()
    }

    private fun updateUserProfileInformation(userId: String) {
        val currentUserDb = mDatabaseReference!!.child(userId)
        currentUserDb.child("firstName").setValue(firstName)
        currentUserDb.child("lastName").setValue(lastName)
    }

    private fun extractMandatoryFields() {
        firstName = etFirstName?.text.toString()
        lastName = etLastName?.text.toString()
        email = etEmail?.text.toString()
        password = etPassword?.text.toString()
    }

    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        verificationSent(mUser)
                    } else {
                        verificationSendingError(task)
                    }
                }
    }

    private fun startLoginActivity() {
        val intent = Intent(this@CreateAccountActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun verificationSendingError(task: Task<Void>) {
        Log.e(TAG, "sendEmailVerification", task.exception)
        Toast.makeText(this@CreateAccountActivity,
                "Ошибка при попытке послать подтверждение регистрации на почту.",
                Toast.LENGTH_SHORT).show()
    }

    private fun verificationSent(mUser: FirebaseUser) {
        Toast.makeText(this@CreateAccountActivity,
                "Подтверждение регистрации выслано на " + mUser.email,
                Toast.LENGTH_SHORT).show()
    }

    private fun noMandatoryFieldsEmpty() =
            (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)
                    && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))

    private fun initialiseFireBase() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()
    }

    private fun initialiseUI() {
        etFirstName = findViewById<View>(R.id.et_first_name) as EditText
        etLastName = findViewById<View>(R.id.et_last_name) as EditText
        etEmail = findViewById<View>(R.id.et_email) as EditText
        etPassword = findViewById<View>(R.id.et_password) as EditText
        btnCreateAccount = findViewById<View>(R.id.btn_register) as Button
        mProgressBar = ProgressDialog(this)
    }
}