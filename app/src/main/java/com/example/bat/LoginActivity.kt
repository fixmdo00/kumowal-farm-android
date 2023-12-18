package com.example.bat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.bat.databinding.ActivityLoginBinding
import com.example.bat.`object`.Pengguna
import com.example.bat.`object`.RQ
import com.example.bat.`object`.SP
import org.json.JSONException

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            binding.loadingProgressBar.visibility = View.VISIBLE
            login(
                binding.itUsername.text.toString(),
                binding.itPassword.text.toString()
            )
        }
    }

    private fun login (username : String, password : String) {

        validateUser(username, password) {
                isValid ->
            if (isValid) {
                Log.d("pzn", "login berhasil")
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                Pengguna.getDetailFromDb(SP.getSP().getString("userId", "null").toString()){
                    binding.loadingProgressBar.visibility = View.GONE
                    when (it){
                        true -> {
                            startActivity(intent)
                            finish()
                        }
                        false -> { }
                    }
                }
            } else {
                binding.loadingProgressBar.visibility = View.GONE
                Log.d("pzn", "login gagal")
                Toast.makeText(this,"Username atau Password salah", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateUser (username : String, password : String,callback: (Boolean) -> Unit) {

        val url = "https://kumowal.my.id/api/user_validate_login.php?username=$username&password=$password"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val status = response.getJSONObject(0).getString("status")
                    when (status) {
                        "berhasil" -> {
                            SP.setLoginPengguna(response.getJSONObject(1).getString("user_id"))
                            callback(true)
                        }
                        "user salah" , "password salah" -> {
                            callback(false)
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                binding.loadingProgressBar.visibility = View.GONE
                Log.d("err",error.toString())
            })
        RQ.getRQ().add(jsonArrayRequest)
    }
}