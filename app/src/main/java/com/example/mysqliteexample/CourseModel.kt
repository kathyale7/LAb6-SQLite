package com.example.mysqliteexample

import java.io.Serializable

 class CourseModel : Serializable {
     var ID:String = ""
     var Descripcion:String = ""
     var Creditos:String = ""
     internal constructor(ID:String, Descripcion:String, Creditos:String){
         this.ID = ID
         this.Descripcion = Descripcion
         this.Creditos = Creditos

     }
}
