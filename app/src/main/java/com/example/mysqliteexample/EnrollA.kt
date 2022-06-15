package com.example.mysqliteexample

class EnrollA private constructor() {
    private var enrollments: ArrayList<Enroll> = ArrayList<Enroll>()

    fun addEnroll(enroll: Enroll){
        enrollments?.add(enroll)
    }
    private object HOLDER {
        val INSTANCE = EnrollA()
    }
    companion object {
        val instance: EnrollA by lazy {
            HOLDER.INSTANCE
        }
    }
 fun getEnroll(description: String): Enroll? {
        for (p: Enroll in enrollments!!){
            if(p.STUDENT_ID.equals(description)){
                return p;
            }
        }
        return null;
    }

    fun getEnrollments(): ArrayList<Enroll>{
        return this.enrollments!!
    }



    fun deleteEnroll(position: Int){
        enrollments!!.removeAt(position)
    }

    fun editEnroll(p: Enroll, position: Int){
        var aux = enrollments!!.get(position)
        aux.STUDENT_ID = p.STUDENT_ID
        aux.COURSE_ID = p.COURSE_ID
    }

}