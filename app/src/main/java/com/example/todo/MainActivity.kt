package com.example.todo

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    var myTodos = ArrayList<Todo>()
    lateinit var recyclerView: RecyclerView
    lateinit var lytNoTodos: LinearLayout
    lateinit var context:Context
    lateinit var todoadapter: Todoadapter

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var firebaseDatabaseReference: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager=LinearLayoutManager(this)
        context =this@MainActivity

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabaseReference=firebaseDatabase.getReference("mytodo")

        lytNoTodos = findViewById(R.id.lytNoTodos)
        todoadapter = Todoadapter(context,myTodos)
        showmyTodos()

        saveToFirebase.setOnClickListener {
            firebaseDatabaseReference.setValue("hello")
        }





        fab.setOnClickListener { view ->
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            */




            val dialog= Dialog(this@MainActivity)
            dialog.setTitle("Enter your Todo")
            dialog.setContentView(R.layout.todo)
            val txttitle = dialog.findViewById<EditText>(R.id.editTitle)
            val txtdescription = dialog.findViewById<EditText>(R.id.editDescription)
            val btnsave = dialog.findViewById<Button>(R.id.btnSave)
            val btndate = dialog.findViewById<Button>(R.id.btndate)

            btnsave.setOnClickListener {
                Log.e("Data","title ${txttitle.text} desc ${txtdescription.text}")
                val sdf = SimpleDateFormat("E dd MMM yyyy hh:mm a")
                val currentdate = sdf.format(Date())
                //myTodos.add(Todo(txttitle.text.toString(),txtdescription.text.toString(), currentdate))
                val mydb=Mytododb(context)
                GlobalScope.launch {
                    mydb.todosDao().saveMyTodo(Todo(0,txttitle.text.toString(),txtdescription.text.toString(), currentdate))
                }

                dialog.dismiss()
                showmyTodos()

            }

            btndate.setOnClickListener {
                val c = Calendar.getInstance();
                var mYear = c.get(Calendar.YEAR);
                var mMonth = c.get(Calendar.MONTH);
                var mDay = c.get(Calendar.DAY_OF_MONTH);
                val datePickerDialog = DatePickerDialog(
                    this,
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        Log.e("Date Selected is",dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                        )
                    }, mYear, mMonth, mDay
                )

                datePickerDialog.show()
            }

            dialog.show()
        }



    }
    fun showmyTodos(){
        Mytododb(context)
            .todosDao()
            .getMyTodos().observe(this, androidx.lifecycle.Observer {mytodoItem->
                myTodos= mytodoItem as ArrayList<Todo>
                todoadapter = Todoadapter(this@MainActivity, myTodos)
                recyclerView.adapter = todoadapter

                val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        todoadapter.removeItem(viewHolder)
                        // todoadapter.notifyDataSetChanged()
                        Toast.makeText(context,"Removing ...",Toast.LENGTH_LONG).show()
                    }


                }

                val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
                itemTouchHelper.attachToRecyclerView(recyclerView)

                if (myTodos.isEmpty()){
                    lytNoTodos.visibility=View.VISIBLE
                }else{
                    lytNoTodos.visibility=View.GONE

                }

            })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                FirebaseAuth.getInstance().signOut()
            return true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }
}
