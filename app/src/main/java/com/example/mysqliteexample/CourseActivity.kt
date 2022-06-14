package com.example.mysqliteexample

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_course.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.idTxt

import kotlinx.android.synthetic.main.activity_main.nameTxt
import kotlinx.android.synthetic.main.activity_main.surnameTxt
import kotlinx.android.synthetic.main.activity_main.updateBtn
import java.util.*
import kotlin.collections.ArrayList

class CourseActivity : AppCompatActivity(), CourseAdapter.onCourseClickListener {

    //In Kotlin `var` is used to declare a mutable variable. On the other hand
    //`internal` means a variable is visible within a given module.
    internal var dbHelper = DatabaseHelper(this)
    var courseA: CourseA = CourseA.instance

    lateinit var lista: RecyclerView
    lateinit var adaptador:CourseAdapter
    lateinit var course: CourseModel
    var archived = ArrayList<CourseModel>()
    var position: Int = 0

    /**
     * Let's create a function to show Toast message
     */
    fun showToast(text: String){
        Toast.makeText(this@CourseActivity, text, Toast.LENGTH_LONG).show()
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
        idTxt.setText("")
    }

    /**
     * Let's override our onCreate method.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

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
        getListOfCourses()

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition: Int = viewHolder.adapterPosition
                val toPosition: Int = target.adapterPosition


                Collections.swap(courseA.getCourses(), fromPosition, toPosition)

                lista.adapter?.notifyItemMoved(fromPosition, toPosition)

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                position = viewHolder.adapterPosition

                if(direction == ItemTouchHelper.LEFT){
                    try {

                        dbHelper.deleteData2(courseA.getCourses()[position].ID)

                    }catch (e: Exception){
                        e.printStackTrace()
                        showToast(e.message.toString())
                    }

                    course = CourseModel(courseA.getCourses()[position].ID, courseA.getCourses()[position].Descripcion, courseA.getCourses()[position].Creditos)
                    courseA.deleteCourse(position)
                    lista.adapter?.notifyItemRemoved(position)

                    Snackbar.make(lista, course.Descripcion + " has been deleted.", Snackbar.LENGTH_LONG).setAction("Undo") {
                        courseA.getCourses().add(position, course)
                        lista.adapter?.notifyItemInserted(position)
                    }.show()
                    adaptador = CourseAdapter(courseA.getCourses(), this@CourseActivity)
                    lista.adapter = adaptador
                }else{

                    position = viewHolder.adapterPosition
                    course = CourseModel(courseA.getCourses()[position].ID, courseA.getCourses()[position].Descripcion, courseA.getCourses()[position].Creditos)
                    archived.add(course)



                   // val i= Intent(this@CrudPersonas, ModificarExample::class.java)
                    //  i.putExtra("puser", personas.getPersonas()[position].user)
                    //  i.putExtra("ppass", personas.getPersonas()[position].password)
                    //  i.putExtra("pname", personas.getPersonas()[position].nombre)
                    //  i.putExtra("poss", position)
                    //  startActivity(i)




                    Snackbar.make(lista, course.Descripcion + " has been modified.", Snackbar.LENGTH_LONG).setAction("Undo") {
                        archived.removeAt(archived.lastIndexOf(course))
                        courseA.getCourses().add(position, course)
                        lista.adapter?.notifyItemInserted(position)
                    }.show()
                    adaptador = CourseAdapter(courseA.getCourses(), this@CourseActivity)
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

        val add: FloatingActionButton = findViewById(R.id.add3)
        add.setOnClickListener { view ->
            Toast.makeText(this, " ", Toast.LENGTH_SHORT).show()
            Snackbar.make(view, "Course inserted.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()


            try {
                course = CourseModel(idTxt.text.toString(),nameTxt.text.toString(),surnameTxt.text.toString())
                courseA.addCourse(course)
                archived.add(course)
                dbHelper.insertData2(idTxt.text.toString(),nameTxt.text.toString(),surnameTxt.text.toString())
                clearEditTexts()
            }catch (e: Exception){
                e.printStackTrace()
                showToast(e.message.toString())
            }


            lista.adapter?.notifyDataSetChanged()
            adaptador = CourseAdapter(courseA.getCourses(), this@CourseActivity)
            lista.adapter = adaptador

//


        }

        val update: FloatingActionButton = findViewById(R.id.update2)
        update.setOnClickListener { view ->
            Toast.makeText(this, " ", Toast.LENGTH_SHORT).show()
            Snackbar.make(view, "Student updated.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()


            try {
                val isUpdate = dbHelper.updateData2(idTxt.text.toString(),
                    nameTxt.text.toString(),
                    surnameTxt.text.toString())

            }catch (e: Exception){
                e.printStackTrace()
                showToast(e.message.toString())
            }
            course = CourseModel(courseA.getCourses()[position].ID,nameTxt.text.toString(),surnameTxt.text.toString())

            courseA.editCourse(course, position)
            clearEditTexts()
            lista.adapter?.notifyDataSetChanged()
            adaptador = CourseAdapter(courseA.getCourses(), this@CourseActivity)
            lista.adapter = adaptador

//


        }


        handleInserts()
        handleUpdates()
      //  handleDeletes()

    }

    /**
     * When our handleInserts button is clicked.
     */
    fun handleInserts() {
/** insertBtn.setOnClickListener {
            try {
                course = CourseModel(idTxt.text.toString(),nameTxt.text.toString(),surnameTxt.text.toString())
                courseA.addCourse(course)
                archived.add(course)
                lista.adapter?.notifyDataSetChanged()
                adaptador = CourseAdapter(courseA.getCourses(), this@CourseActivity)
                lista.adapter = adaptador

                dbHelper.insertData2(idTxt.text.toString(),nameTxt.text.toString(),surnameTxt.text.toString())
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
/**   updateBtn.setOnClickListener {
            try {
                val isUpdate = dbHelper.updateData2(idTxt.text.toString(),
                    nameTxt.text.toString(),
                    surnameTxt.text.toString())

            }catch (e: Exception){
                e.printStackTrace()
                showToast(e.message.toString())
            }

             course = CourseModel(courseA.getCourses()[position].ID,nameTxt.text.toString(),surnameTxt.text.toString())

            courseA.editCourse(course, position)
            Snackbar.make(lista, course.Descripcion + " has been modified.", Snackbar.LENGTH_LONG).setAction("Undo") {
                archived.removeAt(archived.lastIndexOf(course))
                courseA.getCourses().add(position, course)
                lista.adapter?.notifyItemInserted(position)
            }.show()
            adaptador = CourseAdapter(courseA.getCourses(), this@CourseActivity)
            lista.adapter = adaptador
            clearEditTexts()
        } */
    }

    /**
     * When our handleDeletes button is clicked
     */
    fun handleDeletes(){
/**   deleteBtn.setOnClickListener {


            try {

                dbHelper.deleteData2(idTxt.text.toString())

                clearEditTexts()
            }catch (e: Exception){
                e.printStackTrace()
                showToast(e.message.toString())
            }
            course = CourseModel(courseA.getCourses()[position].ID, courseA.getCourses()[position].Descripcion, courseA.getCourses()[position].Creditos)
            courseA.deleteCourse(position)
            lista.adapter?.notifyItemRemoved(position)

            Snackbar.make(lista, course.Descripcion + " has been deleted.", Snackbar.LENGTH_LONG).setAction("Undo") {
                courseA.getCourses().add(position, course)
                lista.adapter?.notifyItemInserted(position)
            }.show()
            adaptador = CourseAdapter(courseA.getCourses(), this@CourseActivity)
            lista.adapter = adaptador
        } */
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
    private fun getListOfCourses() {
        val res = dbHelper.allData2
        if (res.count == 0) {
            showDialog("Error", "No Data Found")
        }

        //val buffer = StringBuffer()
        while (res.moveToNext()) {
           // buffer.append("ID :" + res.getString(0) + "\n")
            // buffer.append("NAME :" + res.getString(1) + "\n")
            // buffer.append("SURNAME :" + res.getString(2) + "\n")
            // buffer.append("AGE :" + res.getString(3) + "\n\n")
            val stu = CourseModel(res.getString(0), res.getString(1), res.getString(2))
            courseA.addCourse(stu)
        }
        val Nstudents = ArrayList<CourseModel>()
        for (p in courseA.getCourses()) {
            Nstudents.add(p)
        }
        adaptador = CourseAdapter(Nstudents, this@CourseActivity)
        lista.adapter = adaptador
    }

    override fun onItemClick(course: CourseModel) {
        nameTxt.setText(course.Descripcion)
        surnameTxt.setText(course.Creditos)
        idTxt.setText(course.ID)
    }


}