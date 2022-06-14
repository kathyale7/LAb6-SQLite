package com.example.mysqliteexample



class Users private constructor() {

    private var users: ArrayList<User> = ArrayList<User>()

    init{

        addUser(User("kat", "123"))
        addUser(User("rol", "123"))
        addUser(User("dan", "123"))

    }

    private object HOLDER {
        val INSTANCE = Users()
    }

    companion object {
        val instance: Users by lazy {
            HOLDER.INSTANCE
        }
    }

    fun addUser(persona: User){
        users?.add(persona)
    }



    fun login(user: String?, password: String?): Boolean{
        for(p: User in users!!){
            if(p.user.equals(user) && p.password.equals(password)){
                return true
            }
        }
        return false
    }

}