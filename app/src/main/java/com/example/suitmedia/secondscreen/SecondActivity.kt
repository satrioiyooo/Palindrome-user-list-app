package com.example.suitmedia.secondscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.example.suitmedia.R
import com.example.suitmedia.thirdscreen.ThirdActivity

class SecondActivity : AppCompatActivity() {
    private lateinit var nextButton: Button
    private lateinit var usernameTextView: TextView
    private lateinit var selectedUserTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        usernameTextView = findViewById(R.id.username)
        selectedUserTextView = findViewById(R.id.selectedUser)
        nextButton = findViewById(R.id.btn_choose)

        // Set the username from the first screen
        val username = intent.getStringExtra("username_input") ?: "User"
        usernameTextView.text = username

        // Set default text for selected user
        selectedUserTextView.text = getString(R.string.username_hasn_t_been_chosen)
        if (username.isEmpty()) {
            usernameTextView.text = getString(R.string.please_fill_in_username_field)
        } else {
            usernameTextView.text = username
        }


        nextButton.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivityForResult(intent, SELECT_USER_REQUEST)
        }
    }

    @SuppressLint("SetTextI18n")
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_USER_REQUEST && resultCode == RESULT_OK) {
            val selectedUser = data?.getStringExtra("selected_user")
            if (!selectedUser.isNullOrEmpty()) {
                selectedUserTextView.text = getString(R.string.username_p1) + selectedUser
            }
        }
    }

    companion object {
        const val SELECT_USER_REQUEST = 1
    }
}