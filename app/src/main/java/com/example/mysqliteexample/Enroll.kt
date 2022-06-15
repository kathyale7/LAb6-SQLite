package com.example.mysqliteexample

import java.io.Serializable


class Enroll : Serializable {

    var STUDENT_ID:String = ""
    var COURSE_ID:String = ""


    internal constructor(STUDENT_ID:String, COURSE_ID:String){
        this.STUDENT_ID = STUDENT_ID
        this.COURSE_ID = COURSE_ID

    }

}