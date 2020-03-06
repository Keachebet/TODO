package com.example.todo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(@PrimaryKey(autoGenerate = true) var id:Int=0,var title:String="", var description:String="", var date:String="",var image_url:String="") {
}

/*@Entity
data class Todo(@PrimaryKey(autoGenerate = true) var id:Int=0,var title:String="", var description:String="", var date:String="") {
}*/