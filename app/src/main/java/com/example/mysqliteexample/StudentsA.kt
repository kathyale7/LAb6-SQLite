package com.example.mysqliteexample

class StudentsA private constructor() {
    private var students: ArrayList<StudentModel> = ArrayList<StudentModel>()

    fun addStudent(student: StudentModel){
        students?.add(student)
    }
    private object HOLDER {
        val INSTANCE = StudentsA()
    }
    companion object {
        val instance: StudentsA by lazy {
            HOLDER.INSTANCE
        }
    }
    fun getStudent(nombre: String): StudentModel? {
        for (p: StudentModel in students!!){
            if(p.Name.equals(nombre)){
                return p;
            }
        }
        return null;
    }

    fun getStudents(): ArrayList<StudentModel>{
        return this.students!!
    }



    fun deleteStudent(position: Int){
        students!!.removeAt(position)
    }

    fun editStudent(p: StudentModel, position: Int){
        var aux = students!!.get(position)
        aux.ID = p.ID
        aux.Name = p.Name
        aux.Surname = p.Surname
        aux.Age = p.Age
    }

}