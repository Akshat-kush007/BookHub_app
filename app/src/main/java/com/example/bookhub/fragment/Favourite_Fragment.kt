package com.example.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.bookhub.R
import com.example.bookhub.adapterClass.DashboardRecyclerAdapter
import com.example.bookhub.adapterClass.FavouriteRecyclerAdapter
import com.example.bookhub.database.BookDataBase
import com.example.bookhub.database.BookEntity


class Favourite_Fragment : Fragment() {

    lateinit var favRecyclerView: RecyclerView
    lateinit var favLayoutManager: LinearLayoutManager
    lateinit var favRecyclerAdapter: FavouriteRecyclerAdapter
    lateinit var favProgressLayout: RelativeLayout
    lateinit var favProgressBar:ProgressBar

    var bookList= listOf<BookEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_favourite_, container, false)

        favRecyclerView=view.findViewById(R.id.favRecycler)
        favProgressLayout=view.findViewById(R.id.favProgressLayout)
        favProgressBar=view.findViewById(R.id.favProgressBar)

        favLayoutManager=GridLayoutManager(activity as Context,2)

        bookList=RetrevingFavourites(activity as Context).execute().get()

        if (activity !=null){
            favProgressLayout.visibility=View.GONE
            favRecyclerAdapter= FavouriteRecyclerAdapter(activity as Context,bookList )
            favRecyclerView.layoutManager=favLayoutManager
            favRecyclerView.adapter=favRecyclerAdapter
        }
        return view

    }

    class RetrevingFavourites(val context: Context): AsyncTask<Void, Void, List<BookEntity>>() {

        override fun doInBackground(vararg p0: Void?): List<BookEntity> {
            val db= Room.databaseBuilder(context,BookDataBase::class.java,"books").build()
            return db.bookDao().getAllBook()
        }

    }


}