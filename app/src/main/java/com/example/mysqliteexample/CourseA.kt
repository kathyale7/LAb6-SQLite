package com.example.mysqliteexample

class CourseA private constructor() {
    private var courses: ArrayList<CourseModel> = ArrayList<CourseModel>()

    fun addCourse(course: CourseModel){
        courses?.add(course)
    }
    private object HOLDER {
        val INSTANCE = CourseA()
    }
    companion object {
        val instance: CourseA by lazy {
            HOLDER.INSTANCE
        }
    }
    fun getCourse(description: String): CourseModel? {
        for (p: CourseModel in courses!!){
            if(p.Descripcion.equals(description)){
                return p;
            }
        }
        return null;
    }

    fun getCourses(): ArrayList<CourseModel>{
        return this.courses!!
    }



    fun deleteCourse(position: Int){
        courses!!.removeAt(position)
    }

    fun editCourse(p: CourseModel, position: Int){
        var aux = courses!!.get(position)
        aux.ID = p.ID
        aux.Descripcion = p.Descripcion
        aux.Creditos = p.Creditos

    }

}