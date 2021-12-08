package com.example.bookhub.adapterClass

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub.R
import com.example.bookhub.activity.DescriptionActivity
import com.example.bookhub.database.BookEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context,val bookList:List<BookEntity>):RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    class FavouriteViewHolder(view:View):RecyclerView.ViewHolder(view){
        val favBookImage:ImageView=view.findViewById(R.id.imgFavBookImage)
        val favBookTitle:TextView=view.findViewById(R.id.txtFavBookTitle)
        val favBookAuthor:TextView=view.findViewById(R.id.txtFavBookAuthor)
        val favBookPrice:TextView=view.findViewById(R.id.txtFavBookPrice)
        val favBookRating:TextView=view.findViewById(R.id.txtFavBookRating)
        val llFavContent:LinearLayout=view.findViewById(R.id.llFavContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_favorite_single_row,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val book=bookList[position]

        holder.favBookTitle.text=book.bookName
        holder.favBookAuthor.text=book.bookAuthor
        holder.favBookPrice.text=book.bookPrice
        holder.favBookRating.text=book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.favBookImage)

        holder.llFavContent.setOnClickListener {
            val intent= Intent(context, DescriptionActivity::class.java)
            intent.putExtra("Id_book",(book.book_id).toString())
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}