package com.landfathich.retrofitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.landfathich.retrofitapp.retrofit.ProductApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.tv)
        val button = findViewById<Button>(R.id.btn)

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
        val productApi = retrofit.create(ProductApi::class.java)

        button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val product = productApi.getProductById(3)
                runOnUiThread {
                    textView.text = product.title
                }
            }
        }
    }
}