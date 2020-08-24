package co.ke.deisaac.retweb.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import co.ke.deisaac.retweb.R


class MainActivity : AppCompatActivity() {
    private lateinit var btnDoSomething: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create object of controls
        val btnDoSomething: Button = findViewById<View>(R.id.btnDoSomething) as Button
        btnDoSomething.setOnClickListener {btnDoSomething()}
    }


    private fun btnDoSomething() {}

}