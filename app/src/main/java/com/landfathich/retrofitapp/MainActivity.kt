package com.landfathich.retrofitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.landfathich.retrofitapp.adapter.ProductAdapter
import com.landfathich.retrofitapp.databinding.ActivityMainBinding
import com.landfathich.retrofitapp.retrofit.MainApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        adapter = ProductAdapter()
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter

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

        binding.sv.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean { // срабатывает когда человек жмет на кнопку поиска на клавиатуре
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean { // срабатывает каждый раз когда есть изменения в тексте searchView
                CoroutineScope(Dispatchers.IO).launch {
                    val products = text?.let { mainApi.getProductsByName(it) }
                    runOnUiThread {
                        binding.apply {
                            adapter.submitList(products?.products)
                        }
                    }
                }
                return true
            }
        })
    }
}