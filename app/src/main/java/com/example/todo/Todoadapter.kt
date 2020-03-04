package com.example.todo

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Todoadapter(var context:Context,
                          var peopleName:List<Todo>): RecyclerView.Adapter<Todoadapter.ViewHolder>() {
    private var removedPosition: Int = 0
    private var removedItem: String = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.todo_list,parent,false))
    }

    override fun getItemCount(): Int {
        return peopleName.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text=peopleName.get(position).title
        holder.description.text=peopleName.get(position).description
        holder.date.text=peopleName.get(position).date

        val char1= peopleName.get(position).title.get(0).toUpperCase().toString()

        val colorGenerator = ColorGenerator.MATERIAL
        val color = colorGenerator.randomColor

        val drawable2 = TextDrawable.builder()
            .buildRound(char1, color)
        holder.image.setImageDrawable(drawable2)

       // holder.image.setImageResource(peopleName.get(position).image)
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder){
        removedPosition = viewHolder.adapterPosition
        removedItem = peopleName[viewHolder.adapterPosition].toString()
        peopleName.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)

        GlobalScope.launch {
            Mytododb.invoke(context).todosDao()
                .deleteTodo(peopleName[viewHolder.adapterPosition+1])

        }

        Snackbar.make(viewHolder.itemView, "$removedItem deleted.", Snackbar.LENGTH_LONG).setAction("UNDO") {
            peopleName.add(removedPosition, removedItem)
            notifyItemInserted(removedPosition)
        }.show()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val description = itemView.findViewById<TextView>(R.id.description)
        val date = itemView.findViewById<TextView>(R.id.date)
        val image = itemView.findViewById<ImageView>(R.id.imageView)


    }

}

private fun <E> List<E>.add(removedPosition: Int, removedItem: String) {

}

private fun <E> List<E>.removeAt(position: Int) {
}
