package com.example.todo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Todo(@PrimaryKey(autoGenerate = true) var id:Int=0,var title:String="",
                var description:String="", var date:String="",var image_url:String=""):Serializable {
}

/*@Entity
data class Todo(@PrimaryKey(autoGenerate = true) var id:Int=0,var title:String="", var description:String="", var date:String="") {
}*/