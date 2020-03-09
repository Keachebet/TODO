package com.example.todo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_images_page2.*
import kotlinx.android.synthetic.main.todo_list.*

class ImagesPage : AppCompatActivity() {
lateinit var context:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_page2)
        context = this@ImagesPage
        val todo:Todo = intent.getSerializableExtra("todo") as Todo
        Glide
            .with(this)
            .load(todo.image_url)
            .centerCrop()
            .into(imgs);

        texttitle.text = todo.title
        txtdescribe.text = todo.description

       // Toast.makeText(context, todo.toString(),Toast.LENGTH_SHORT).show()



    }
}
