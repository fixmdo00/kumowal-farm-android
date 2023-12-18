package com.example.bat.`object`

import android.content.Context
import android.content.SharedPreferences

object SP {

    lateinit var LoginSP : SharedPreferences

    fun initLoginSP (context: Context){
        val sp = context.getSharedPreferences("loginSP", Context.MODE_PRIVATE)
        LoginSP = sp
    }

    fun setLoginPengguna(user_id : String){
        LoginSP.edit().putBoolean("loginStatus", true).apply()
        LoginSP.edit().putString("userId", user_id).apply()
    }

    fun getSP () : SharedPreferences {
        return LoginSP
    }

    fun getUserId() : String {
        return LoginSP.getString("userId", "null")!!
    }

    fun isLogin () : Boolean {
        return LoginSP.getBoolean("loginStatus", false)
    }

    fun hapusLoginPengguna (){
        LoginSP.edit().putBoolean("loginStatus", false).apply()
        LoginSP.edit().putString("userId", null).apply()
    }

}