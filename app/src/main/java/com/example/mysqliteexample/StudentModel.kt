package com.example.mysqliteexample

import java.io.Serializable

 class StudentModel : Serializable {
     var ID:String = ""
     var Name:String = ""
     var Surname:String = ""
     var Age:String = ""
     internal constructor(ID:String, Name:String, Surname:String, Age:String){
         this.ID = ID
         this.Name = Name
         this.Surname = Surname
         this.Age = Age

     }
}
