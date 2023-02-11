package com.utad.api_peliculas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var rvGeneros: RecyclerView
    val TAG = "MainActivity"
    var data: ArrayList<Genre> = ArrayList()
    
    private var adapter: GenreAdapter? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //loader = findViewById<ProgressBar>(R.id.progressbar)
        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        rvGeneros = findViewById<RecyclerView>(R.id.rvGeneros)

        // Mostrar como cuadricula
        val mLayoutManager = GridLayoutManager(this, 2)
        rvGeneros.layoutManager = mLayoutManager
        //Creamos el adapter y lo vinculamos con el recycle

        adapter = GenreAdapter(data) {

            val fragment = VistaPeliculas()
            val bundle = Bundle()
            bundle.putSerializable("miGenero",it)
            fragment.arguments = bundle
            supportFragmentManager?.beginTransaction()
                ?.replace(R.id.mainContainer, fragment)?.addToBackStack("MainActivity")?.commit()
        }
        rvGeneros.adapter = adapter


        ApiRest.initService()
        getGenres()

        swipeRefreshLayout.setOnRefreshListener {
            getGenres()
        }

    }

    private fun getGenres() {
        val call = ApiRest.service.getGenres()
        call.enqueue(object : Callback<Generos> {
            override fun onResponse(call: Call<Generos>, response: Response<Generos>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    data.clear()
                    data.addAll(body.genres)
                    Log.i(TAG,data.toString())
                    adapter?.notifyItemRangeChanged(0,data.size)
                } else {
                    Log.e(TAG, response.errorBody()?.string() ?: "error")
                }

                //loader?.isVisible = false
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<Generos>, t: Throwable) {
                Log.e(TAG, t.message.toString())
               // loader?.isVisible = false
                swipeRefreshLayout.isRefreshing= false
            }
        })
    }

}