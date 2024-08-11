package com.example.suitmedia

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.suitmedia.secondscreen.SecondActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private lateinit var usernameInput: TextInputEditText
    private lateinit var palindromeInput: TextInputEditText
    private lateinit var checkButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        usernameInput = findViewById(R.id.username_input)
        palindromeInput = findViewById(R.id.text_input)
        checkButton = findViewById(R.id.check)
        nextButton = findViewById(R.id.next)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        checkButton.setOnClickListener {
            val text = palindromeInput.text.toString()
            if (isPalindrome(text)) {
                showDialog(getString(R.string.ispalindrome))
            } else {
                showDialog(getString(R.string.not_palindrome))
            }
        }

        nextButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra(getString(R.string.username_input), usernameInput.text.toString())
            startActivity(intent)
        }
    }

    private fun isPalindrome(text: String): Boolean {
        val cleanText = text.toLowerCase().replace(Regex("[^a-z0-9]"), "")
        return cleanText == cleanText.reversed()
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}