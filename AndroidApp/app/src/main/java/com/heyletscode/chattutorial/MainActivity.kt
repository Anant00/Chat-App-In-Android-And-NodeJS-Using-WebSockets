package com.heyletscode.chattutorial

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.heyletscode.utils.PermissionManager
import com.heyletscode.utils.showLongToast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PermissionManager.requestStoragePermission(this)
        val editText = findViewById<EditText>(R.id.editText)
        findViewById<View>(R.id.enterBtn)
                .setOnClickListener {
                    val intent = Intent(this, ChatActivity::class.java)
                    if (!editText.text.isNullOrEmpty()) {
                        intent.putExtra("name", editText.text.toString())
                        startActivity(intent)
                    } else {
                        showLongToast("Name Expected, Enter your name to continue")
                    }
                }
    }
}