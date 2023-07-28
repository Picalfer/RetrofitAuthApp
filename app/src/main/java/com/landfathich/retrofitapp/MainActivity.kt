package com.landfathich.retrofitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.landfathich.retrofitapp.databinding.ActivityMainBinding
import com.landfathich.retrofitapp.retrofit.AuthRequest
import com.landfathich.retrofitapp.retrofit.MainApi
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create()) // подключаем конвертер, который будет превращать gson формат в data класс
            .client(client)
            .build()
        val mainApi = retrofit.create(MainApi::class.java)

        binding.btn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val user = mainApi.auth(
                    AuthRequest(
                        binding.username.text.toString(),
                        binding.password.text.toString()
                    )
                )
                runOnUiThread {
                    binding.firstName.text = user.firstName
                    binding.secondName.text = user.lastName
                    Picasso.get()
                        .load(user.image)
                        .into(binding.iv)
                }
            }
        }
    }
}