package com.utad.api_peliculas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VistaPeliculas : Fragment() {
    val TAG = "MainActivity"
    var data: ArrayList<PeliculasResponse.Pelicula> = ArrayList()
    private var adapter: PeliculasAdapter? = null
    private lateinit var rvPeliculas: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vista_peliculas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPeliculas = view.findViewById<RecyclerView>(R.id.rvPeliculas)
        // Mostrar como cuadricula
        val mLayoutManager = GridLayoutManager(this.context, 2)
        rvPeliculas.layoutManager = mLayoutManager
        adapter = PeliculasAdapter(data){
            val fragment = FragmentPelicula()
            val bundle = Bundle()
            bundle.putSerializable("miPelicula",it)
            fragment.arguments = bundle
            parentFragmentManager?.beginTransaction()
                ?.replace(R.id.peliculasContainer, fragment)?.addToBackStack("MainActivity")?.commit()
        }
        rvPeliculas.adapter = adapter

        // recupero el genero de la vista anterior
        val genero = arguments?.getSerializable("miGenero") as Genre
        getPeliculas(genero.id)

    }

    private fun getPeliculas(idGenero: Int){

        val call = ApiRest.service.getMovieByGenre(idGenero)
        call.enqueue(object : Callback<PeliculasResponse> {
            override fun onResponse(call: Call<PeliculasResponse>, response: Response<PeliculasResponse>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    data.clear()
                    data.addAll(body.results)
                    Log.i(TAG,data.toString())
                    adapter?.notifyItemRangeChanged(0,data.size)
                } else {
                    Log.e(TAG, response.errorBody()?.string() ?: "error")
                }

            }

            override fun onFailure(call: Call<PeliculasResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
        })

    }


}