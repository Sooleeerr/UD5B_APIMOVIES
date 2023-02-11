package com.utad.api_peliculas

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentPelicula.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentPelicula : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pelicula, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pelicula = arguments?.getSerializable("miPelicula") as PeliculasResponse.Pelicula
        view?.findViewById<TextView>(R.id.tituloPelicula)?.setText(pelicula.title)
        view?.findViewById<TextView>(R.id.descripcionPelicula)?.setText(pelicula.overview)

        /*val dateString = pelicula.release_date
        val formatter = DateTimeFormatter.ofPattern("yyyy/mm/dd")
        val date = LocalDate.parse(dateString, formatter).toString()*/

        val dateString = pelicula.release_date
        val yearLanzamiento= dateString.subSequence(0,4)

        view?.findViewById<TextView>(R.id.yearPelicula)?.setText(yearLanzamiento)
        view?.findViewById<TextView>(R.id.notaPelicula)?.setText(pelicula.vote_average.toString())
        Picasso.get().load(ApiRest.URL_IMAGES + pelicula.poster_path).into(view?.findViewById<ImageView>(R.id.imagenPelicula))


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentPelicula.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentPelicula().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}