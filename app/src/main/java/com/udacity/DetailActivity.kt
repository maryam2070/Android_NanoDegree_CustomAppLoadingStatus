package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        if(intent.hasExtra("status"))
            status_tv.text=intent.getStringExtra("status")
        if(intent.hasExtra("file_name"))
            file_name_tv.text=intent.getStringExtra("file_name")

        back_btn.setOnClickListener {
            finish()
        }

    }

}
