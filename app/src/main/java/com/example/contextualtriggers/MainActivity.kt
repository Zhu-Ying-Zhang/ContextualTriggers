package com.example.contextualtriggers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.contextualtriggers.ui.theme.ContextualTriggersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ContextTriggers", "Application started")
        //Start the ContentUpdateManager service
        val intent = Intent(this, ContextUpdateManager::class.java)
        startForegroundService(intent)

        finish()
    }
}