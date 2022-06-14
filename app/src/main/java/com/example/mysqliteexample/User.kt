package com.example.mysqliteexample

import java.io.Serializable


class User : Serializable {

    var user:String = ""
    var password:String = ""


    internal constructor(user:String, password:String){
        this.user = user
        this.password = password

    }

}