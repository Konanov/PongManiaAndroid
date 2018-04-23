package com.pongmania.konanov.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
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

    private val TAG = "CreateAccountActivity"

    @BindView(R.id.et_first_name) lateinit var etFirstName: EditText
    @BindView(R.id.et_last_name) lateinit var etLastName: EditText
    @BindView(R.id.create_email) lateinit var createEmail: EditText
    @BindView(R.id.et_password) lateinit var etPassword: EditText
    private lateinit var mProgressBar: ProgressBar

    //Firebase references
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDatabase: FirebaseDatabase

    private lateinit var mAuth: FirebaseAuth

    //global variables
    private lateinit var firstName: String
    private lateinit var lastName: String
    lateinit var email: String
    lateinit var password: String

    @Inject
    lateinit var retrofit: Retrofit

    @OnClick(R.id.btn_register)
    fun registerUser() {
        createNewAccount()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        (application as PongMania).webComponent.inject(this)
        ButterKnife.bind(this)
        mProgressBar = ProgressBar(this)

        initialiseFireBase()
    }

    private fun createNewAccount() {
        extractMandatoryFields()
        if (noMandatoryFieldsEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        mProgressBar.visibility = View.GONE

                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")

                            val userId = mAuth.currentUser!!.uid
                            verifyEmail()
                            updateUserProfileInformation(userId)
                            tryCreateUser()
                            CredentialsPreference.setCredentials(this.application, email, password)

                            goFor(this@CreateAccountActivity, LoginActivity::class.java)
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
                .createUser(Player.Credentials(email, firstName, lastName, password))
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
        val currentUserDb = mDatabaseReference.child(userId)
        currentUserDb.child("firstName").setValue(firstName)
        currentUserDb.child("lastName").setValue(lastName)
    }

    private fun extractMandatoryFields() {
        firstName = etFirstName.text.toString()
        lastName = etLastName.text.toString()
        email = createEmail.text.toString()
        password = etPassword.text.toString()
    }

    private fun verifyEmail() {
        val mUser = mAuth.currentUser
        mUser!!.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        verificationSent(mUser)
                    } else {
                        verificationSendingError(task)
                    }
                }
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
        mDatabaseReference = mDatabase.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()
    }
}