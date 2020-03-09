package com.example.todo

import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.media.RingtoneManager
import android.os.Build
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
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
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

    val PICK_IMAGE=300
    var image_url=""


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
        fetchMyTodoFromFirebase()

        this.title= getString(R.string.app_name)

        saveToFirebase.setOnClickListener {

            val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)
            val channelId = "my_channel"
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Test Notification")
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
            }




button.setOnClickListener {
    val intent =  Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);



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
            //val btndate = dialog.findViewById<Button>(R.id.btn_image)
            val btn_image = dialog.findViewById<Button>(R.id.btn_image)
            btn_image.setOnClickListener {
                val intent =  Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);


            }

            btnsave.setOnClickListener {
                Log.e("Data","title ${txttitle.text} desc ${txtdescription.text}")
                val sdf = SimpleDateFormat("E dd MMM yyyy hh:mm a")
                val currentdate = sdf.format(Date())

                //first we will upload the image once the image is upload and we received the url.
                //save to rest to firebase.
                if(txttitle.text.toString().isEmpty()){
                    txttitle.error="Empty set"
                }

                else {
                    if (txtdescription.text.toString().isEmpty()) {
                        txtdescription.error = "Provide description"
                    }
                    else {
                        val todo = Todo(
                            Date().time.toInt(), txttitle.text.toString(), txtdescription.text.toString(),
                            currentdate, image_url
                        )
                        firebaseDatabaseReference.push().setValue(todo)

                        dialog.dismiss()
                        showmyTodos()

                    }
                }

                }




           /* btndate.setOnClickListener {
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
            }*/

            dialog.show()
        }



    }
    fun fetchMyTodoFromFirebase(){
        firebaseDatabaseReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                if(dataSnapshot.exists()){
                    val todo: Todo = dataSnapshot.getValue(Todo::class.java)!!

                    val mydb=Mytododb(context)
                    GlobalScope.launch {
                        mydb.todosDao().saveMyTodo(todo)
                    }
                    //myTodos.add(todo)
                    //showOnRecyclerView(myTodos)
                }else{
                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
    fun showmyTodos(){
        Mytododb(context)
            .todosDao()
            .getMyTodos().observe(this, androidx.lifecycle.Observer { mytodoItem ->
                myTodos = mytodoItem as ArrayList<Todo>
                showOnRecyclerView(myTodos)
            })
    }

    fun showOnRecyclerView(todosList:List<Todo>){
        todoadapter = Todoadapter(this@MainActivity, todosList)
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

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun showToast(message:String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
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
            R.id.action_change_language->{
                //change language
                changeLanguage("sw")
                return true;
            }
            else -> super.onOptionsItemSelected(item)
        }


    }

    fun changeLanguage(language:String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res: Resources = this.getResources()
        val config =
            Configuration(res.getConfiguration())
        config.locale = locale
        res.updateConfiguration(config, res.getDisplayMetrics())
        val refresh = Intent(this@MainActivity,MainActivity::class.java)
        finish()
        startActivity(refresh)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imagedata: Intent?) {
        super.onActivityResult(requestCode, resultCode, imagedata)
        if(requestCode==PICK_IMAGE){
           // showToast("Result Returned "+imagedata.toString())
            //imageView3.setImageURI(data!!.data)
            //save to firebase
            val storage = FirebaseStorage.getInstance().reference.child("images")
            //storage.putFile(imagedata!!.data!!)
            val uploadTask =storage.putFile(imagedata!!.data!!)
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storage.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    showToast("Saved Successfully ")
                    //Log.e("IMAGE URL",downloadUri.toString())
                    image_url = downloadUri.toString()

                } else {
                    // Handle failures
                    // ...

                }
            }
        }
    }

}
