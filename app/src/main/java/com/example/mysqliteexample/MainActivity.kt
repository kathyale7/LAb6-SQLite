package com.example.mysqliteexample

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), StudentAdapter.onStudentClickListener {

    //In Kotlin `var` is used to declare a mutable variable. On the other hand
    //`internal` means a variable is visible within a given module.
    internal var dbHelper = DatabaseHelper(this)
    var studentsA: StudentsA = StudentsA.instance

    lateinit var lista: RecyclerView
    lateinit var adaptador:StudentAdapter
    lateinit var student: StudentModel
    var archived = ArrayList<StudentModel>()
    var position: Int = 0

    /**
     * Let's create a function to show Toast message
     */
    fun showToast(text: String){
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
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

    /**
     * Let's create a method to clear our edittexts
     */
    fun clearEditTexts(){
        nameTxt.setText("")
        surnameTxt.setText("")
        typeTxt.setText("")
        idTxt.setText("")
    }

    /**
     * Let's override our onCreate method.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchIcon = findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)

        val cancelIcon = findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.BLACK)


        val textView = findViewById<TextView>(R.id.search_src_text)
        textView.setTextColor(Color.BLACK)

        lista = findViewById(R.id.lista)
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
        getListOfStudents()

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition: Int = viewHolder.adapterPosition
                val toPosition: Int = target.adapterPosition


                Collections.swap(studentsA.getStudents(), fromPosition, toPosition)

                lista.adapter?.notifyItemMoved(fromPosition, toPosition)

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                position = viewHolder.adapterPosition

                if(direction == ItemTouchHelper.LEFT){
                    try {

                        dbHelper.deleteData(studentsA.getStudents()[position].ID)
                        showToast("Deleted")
                    }catch (e: Exception){
                        e.printStackTrace()
                        showToast(e.message.toString())
                    }

                    student = StudentModel(studentsA.getStudents()[position].ID, studentsA.getStudents()[position].Name, studentsA.getStudents()[position].Surname, studentsA.getStudents()[position].Age)
                    studentsA.deleteStudent(position)
                    lista.adapter?.notifyItemRemoved(position)

                    Snackbar.make(lista, student.Name + " has been deleted.", Snackbar.LENGTH_LONG).setAction("Undo") {
                        studentsA.getStudents().add(position, student)
                        lista.adapter?.notifyItemInserted(position)
                    }.show()
                    adaptador = StudentAdapter(studentsA.getStudents(), this@MainActivity)
                    lista.adapter = adaptador
                }else{

                    position = viewHolder.adapterPosition
                    student = StudentModel(studentsA.getStudents()[position].ID, studentsA.getStudents()[position].Name, studentsA.getStudents()[position].Surname, studentsA.getStudents()[position].Age)
                    archived.add(student)

                    nameTxt.setText(studentsA.getStudents()[position].Name)
                    surnameTxt.setText(studentsA.getStudents()[position].Surname)
                    typeTxt.setText(studentsA.getStudents()[position].Age)
                    idTxt.setText(studentsA.getStudents()[position].ID)




                    Snackbar.make(lista, student.Name + " has been modified.", Snackbar.LENGTH_LONG).setAction("Undo") {
                        archived.removeAt(archived.lastIndexOf(student))
                        studentsA.getStudents().add(position, student)
                        lista.adapter?.notifyItemInserted(position)
                    }.show()
                    adaptador = StudentAdapter(studentsA.getStudents(), this@MainActivity)
                    lista.adapter = adaptador


                }}
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

        val add: FloatingActionButton = findViewById(R.id.add2)
        add.setOnClickListener { view ->
            Toast.makeText(this, " ", Toast.LENGTH_SHORT).show()
            Snackbar.make(view, "Student inserted.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()


            try {
                student = StudentModel(idTxt.text.toString(),nameTxt.text.toString(),surnameTxt.text.toString(),
                    typeTxt.text.toString())
                studentsA.addStudent(student)
                archived.add(student)
                dbHelper.insertData(idTxt.text.toString(),nameTxt.text.toString(),surnameTxt.text.toString(),
                    typeTxt.text.toString())
                clearEditTexts()
            }catch (e: Exception){
                e.printStackTrace()
                showToast(e.message.toString())
            }


            lista.adapter?.notifyDataSetChanged()
            adaptador = StudentAdapter(studentsA.getStudents(), this@MainActivity)
            lista.adapter = adaptador

//


        }

        val update: FloatingActionButton = findViewById(R.id.update1)
        update.setOnClickListener { view ->
            Toast.makeText(this, " ", Toast.LENGTH_SHORT).show()
            Snackbar.make(view, "Student updated.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()


            try {
                val isUpdate = dbHelper.updateData(idTxt.text.toString(),
                    nameTxt.text.toString(),
                    surnameTxt.text.toString(),
                    typeTxt.text.toString())

            }catch (e: Exception){
                e.printStackTrace()
                showToast(e.message.toString())
            }
            student = StudentModel(studentsA.getStudents()[position].ID,nameTxt.text.toString(),surnameTxt.text.toString(), typeTxt.text.toString())

            studentsA.editStudent(student, position)
            clearEditTexts()
            lista.adapter?.notifyDataSetChanged()
            adaptador = StudentAdapter(studentsA.getStudents(), this@MainActivity)
            lista.adapter = adaptador

//


        }


       // handleInserts()
        //handleUpdates()
        //handleDeletes()

    }

    /**
     * When our handleInserts button is clicked.
     */
    fun handleInserts() {
/**   insertBtn.setOnClickListener {
            try {
                student = StudentModel(idTxt.text.toString(),nameTxt.text.toString(),surnameTxt.text.toString(),
                    typeTxt.text.toString())
                studentsA.addStudent(student)
                archived.add(student)
                lista.adapter?.notifyDataSetChanged()
                adaptador = StudentAdapter(studentsA.getStudents(), this@MainActivity)
                lista.adapter = adaptador

                dbHelper.insertData(idTxt.text.toString(),nameTxt.text.toString(),surnameTxt.text.toString(),
                    typeTxt.text.toString())
                clearEditTexts()
            }catch (e: Exception){
                e.printStackTrace()
                showToast(e.message.toString())
            }
        }  */
    }

    /**
     * When our handleUpdates data button is clicked
     */
    fun handleUpdates() {
/**  updateBtn.setOnClickListener {
            try {
                val isUpdate = dbHelper.updateData(idTxt.text.toString(),
                    nameTxt.text.toString(),
                    surnameTxt.text.toString(),
                    typeTxt.text.toString())

            }catch (e: Exception){
                e.printStackTrace()
                showToast(e.message.toString())
            }

             student = StudentModel(studentsA.getStudents()[position].ID,nameTxt.text.toString(),surnameTxt.text.toString(),
                typeTxt.text.toString())

            studentsA.editStudent(student, position)
            Snackbar.make(lista, student.Name + " has been modified.", Snackbar.LENGTH_LONG).setAction("Undo") {
                archived.removeAt(archived.lastIndexOf(student))
                studentsA.getStudents().add(position, student)
                lista.adapter?.notifyItemInserted(position)
            }.show()
            adaptador = StudentAdapter(studentsA.getStudents(), this@MainActivity)
            lista.adapter = adaptador
            clearEditTexts()
        }  */
    }

    /**
     * When our handleDeletes button is clicked
     */
    fun handleDeletes(){
        // deleteBtn.setOnClickListener {


        //     try {

        //         dbHelper.deleteData(idTxt.text.toString())

        //         clearEditTexts()
        //     }catch (e: Exception){
        //         e.printStackTrace()
        //         showToast(e.message.toString())
        //     }
        //     student = StudentModel(studentsA.getStudents()[position].ID, studentsA.getStudents()[position].Name, studentsA.getStudents()[position].Surname, studentsA.getStudents()[position].Age)
        //     studentsA.deleteStudent(position)
        //     lista.adapter?.notifyItemRemoved(position)
        //
        //     Snackbar.make(lista, student.Name + " has been deleted.", Snackbar.LENGTH_LONG).setAction("Undo") {
                //         studentsA.getStudents().add(position, student)
        //        lista.adapter?.notifyItemInserted(position)
        //    }.show()
        //    adaptador = StudentAdapter(studentsA.getStudents(), this@MainActivity)
        //    lista.adapter = adaptador
        //  }
    }

    /**
     * When our View All is clicked
     */



    fun handleSearch()  {
       // btnSearch.setOnClickListener(
        //     View.OnClickListener {
        //         val res = dbHelper.searchData(idTxt.text.toString())
        //        if (res.count == 0) {
        //           showDialog("Error", "No Data Found")
        //            return@OnClickListener
        //       }
        //       val buffer = StringBuffer()
        //      while (res.moveToNext()) {
                    //             buffer.append("ID :" + res.getString(0) + "\n")
        //           buffer.append("NAME :" + res.getString(1) + "\n")
        //          buffer.append("SURNAME :" + res.getString(2) + "\n")
        //           //             buffer.append("AGE :" + res.getString(3) + "\n\n")
        //          //         }
        //       showDialog("Dato", buffer.toString())
        //     }
    //  )
    }
    private fun getListOfStudents() {
        val res = dbHelper.allData

        if (res.count == 0) {
            showDialog("Error", "No Data Found")
        }

        //val buffer = StringBuffer()
        while (res.moveToNext()) {



            val stu = StudentModel(res.getString(0), res.getString(1), res.getString(2), res.getString(3))
            studentsA.addStudent(stu)
        }
        val Nstudents = ArrayList<StudentModel>()
        for (p in studentsA.getStudents()) {
            Nstudents.add(p)
        }
        adaptador = StudentAdapter(Nstudents, this@MainActivity)
        lista.adapter = adaptador
    }

    override fun onItemClick(student: StudentModel) {
        nameTxt.setText(student.Name)
        surnameTxt.setText(student.Surname)
        typeTxt.setText(student.Age)
        idTxt.setText(student.ID)
    }


}