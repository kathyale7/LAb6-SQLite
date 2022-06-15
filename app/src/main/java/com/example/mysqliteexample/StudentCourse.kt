package com.example.mysqliteexample

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class StudentCourse : AppCompatActivity(), EnrollAdapter.onEnrollClickListener {

    internal var dbHelper = DatabaseHelper(this)
    var enrollA: EnrollA = EnrollA.instance

    lateinit var lista: RecyclerView
    lateinit var adaptador:EnrollAdapter
    lateinit var enroll: Enroll
    var archived = ArrayList<Enroll>()
    var position: Int = 0

    /**
     * Let's create a function to show Toast message
     */
    fun showToast(text: String){
        Toast.makeText(this@StudentCourse, text, Toast.LENGTH_LONG).show()
    }

    /**
     * Let's create a function to show an alert dialog with data dialog.
     */
    fun showDialog(title : String,Message : String){
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(Message)
        builder.show()
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_course)
        val searchIcon = findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)

        val cancelIcon = findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.BLACK)


        val textView = findViewById<TextView>(R.id.search_src_text)
        textView.setTextColor(Color.BLACK)

        lista = findViewById(R.id.listasc)
        lista.layoutManager = LinearLayoutManager(lista.context)
        lista.setHasFixedSize(true)

        findViewById<SearchView>(R.id.person_search).setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adaptador.filter.filter(newText)
                return false
            }
        })
        getListOfEnrollments()

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition: Int = viewHolder.adapterPosition
                val toPosition: Int = target.adapterPosition


                Collections.swap(enrollA.getEnrollments(), fromPosition, toPosition)

                lista.adapter?.notifyItemMoved(fromPosition, toPosition)

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                position = viewHolder.adapterPosition

                if(direction == ItemTouchHelper.LEFT){
                    try {

                        dbHelper.deleteData3(enrollA.getEnrollments()[position].STUDENT_ID)
                        showToast("Deleted")
                    }catch (e: Exception){
                        e.printStackTrace()
                        showToast(e.message.toString())
                    }

                    enroll = Enroll(enrollA.getEnrollments()[position].STUDENT_ID, enrollA.getEnrollments()[position].COURSE_ID)

                    enrollA.deleteEnroll(position)
                    lista.adapter?.notifyItemRemoved(position)

                    Snackbar.make(lista, " Item has been deleted.", Snackbar.LENGTH_LONG).setAction("Undo") {
                        enrollA.getEnrollments().add(position, enroll)
                        lista.adapter?.notifyItemInserted(position)
                    }.show()
                    adaptador = EnrollAdapter(enrollA.getEnrollments(), this@StudentCourse)
                    lista.adapter = adaptador
                }else{

                    position = viewHolder.adapterPosition

                    enroll = Enroll(enrollA.getEnrollments()[position].STUDENT_ID, enrollA.getEnrollments()[position].COURSE_ID)

                    archived.add(enroll)






                 /*   Snackbar.make(lista, student.Name + " has been modified.", Snackbar.LENGTH_LONG).setAction("Undo") {
                        archived.removeAt(archived.lastIndexOf(student))
                        studentsA.getStudents().add(position, student)
                        lista.adapter?.notifyItemInserted(position)
                    }.show()
                    adaptador = StudentAdapter(studentsA.getStudents(), this@MainActivity)
                    lista.adapter = adaptador*/


                }
            }
            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                //RecyclerViewSwipeDecorator.Builder(this@MainActivity, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                //    .addSwipeLeftBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.red))
                //    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                //    .addSwipeRightBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.green))
                //    .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                //   .create()
                //    .decorate()
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
    }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(lista)
    }

    override fun onItemClick(course: Enroll) {
        showDialog("Good", "All Good")
    }
    private fun getListOfEnrollments() {
        val res = dbHelper.allData3

        if (res.count == 0) {
            showDialog("Error", "No Data Found")
        }

        //val buffer = StringBuffer()
        while (res.moveToNext()) {



            val stu = Enroll(res.getString(0), res.getString(1))
            enrollA.addEnroll(stu)
        }
        val Nstudents = ArrayList<Enroll>()
        for (p in enrollA.getEnrollments()) {
            Nstudents.add(p)
        }
        adaptador = EnrollAdapter(Nstudents, this@StudentCourse)
        lista.adapter = adaptador
    }
}