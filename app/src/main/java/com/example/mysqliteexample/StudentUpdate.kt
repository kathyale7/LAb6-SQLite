package com.example.mysqliteexample

import android.app.AlertDialog
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_student_update.*

class StudentUpdate : AppCompatActivity(), StudentAdapter.onStudentClickListener,  CourseAdapter.onCourseClickListener {
    internal var dbHelper = DatabaseHelper(this)
    var courseA: CourseA = CourseA.instance
    var studentsA: StudentsA = StudentsA.instance
    var enrollA: EnrollA = EnrollA.instance

    lateinit var listaS: RecyclerView
    lateinit var listaC: RecyclerView
    lateinit var adaptador1:CourseAdapter
    lateinit var course: CourseModel
    var archivedC = ArrayList<CourseModel>()

    lateinit var adaptador2:StudentAdapter
    lateinit var student: StudentModel
    var archivedS = ArrayList<StudentModel>()

    lateinit var enroll: Enroll

    var position: Int = 0

    fun showToast(text: String){
        Toast.makeText(this@StudentUpdate, text, Toast.LENGTH_LONG).show()
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
        StudentIDTxt.setText("")
        CourseIDTxt.setText("") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_update)

        val searchIcon = findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)

        val cancelIcon = findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.BLACK)


        val textView = findViewById<TextView>(R.id.search_src_text)
        textView.setTextColor(Color.BLACK)

        listaS = findViewById(R.id.lista_student)
        listaS.layoutManager = LinearLayoutManager(listaS.context)
        listaS.setHasFixedSize(true)

        listaC = findViewById(R.id.lista_course)
        listaC.layoutManager = LinearLayoutManager(listaC.context)
        listaC.setHasFixedSize(true)

        findViewById<SearchView>(R.id.person_search).setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adaptador1.filter.filter(newText)
                return false
            }
        })

        getListOfCourses()
        getListOfStudents()


        val add: FloatingActionButton = findViewById(R.id.add5)
        add.setOnClickListener { view ->
            Toast.makeText(this, " ", Toast.LENGTH_SHORT).show()
            Snackbar.make(view, "Enrollment inserted.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()


            try {
                enroll = Enroll(StudentIDTxt.text.toString(),CourseIDTxt.text.toString())
                enrollA.addEnroll(enroll)

                dbHelper.insertData4(StudentIDTxt.text.toString(),CourseIDTxt.text.toString())
                showToast("Enrollment good")
                clearEditTexts()
            }catch (e: Exception){
                e.printStackTrace()
                showToast(e.message.toString())
            }




//


        }
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
            adaptador2 = StudentAdapter(Nstudents, this@StudentUpdate)
            listaS.adapter = adaptador2
        }

        private fun getListOfCourses() {
            val res = dbHelper.allData2
            if (res.count == 0) {
                showDialog("Error", "No Data Found")
            }


            while (res.moveToNext()) {

                val stu = CourseModel(res.getString(0), res.getString(1), res.getString(2))
                courseA.addCourse(stu)
            }
            val Nstudents = ArrayList<CourseModel>()
            for (p in courseA.getCourses()) {
                Nstudents.add(p)
            }
            adaptador1 = CourseAdapter(Nstudents, this@StudentUpdate)
            listaC.adapter = adaptador1
        }

    override fun onItemClick(course: CourseModel) {
        CourseIDTxt.setText(course.ID)
    }

    override fun onItemClick(student: StudentModel) {
        StudentIDTxt.setText(student.ID)
    }

}