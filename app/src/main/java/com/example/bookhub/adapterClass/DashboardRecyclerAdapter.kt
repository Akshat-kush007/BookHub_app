package com.example.bookhub.adapterClass

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub.R
import com.example.bookhub.activity.DescriptionActivity
import com.example.bookhub.models.Book
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class DashboardRecyclerAdapter(val context:Context,val itemList:ArrayList<Book>):RecyclerView.Adapter<DashboardRecyclerAdapter.DashBoardViewHolder>() {

    class DashBoardViewHolder(view:View):RecyclerView.ViewHolder(view){
//        val textView:TextView=view.findViewById(R.id.txtBookName)
        val bookname:TextView=view.findViewById(R.id.txtBookName)
        val ratting:TextView=view.findViewById(R.id.txtRatings)
        val price:TextView=view.findViewById(R.id.txtPrice)
        val aurthorName:TextView=view.findViewById(R.id.txtAurthorName)
        val image:ImageView=view.findViewById(R.id.imgRecycler_view_image)
        val lItem:RelativeLayout=view.findViewById(R.id.lItem)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.dashboard_view_layout,parent,false)
        return DashBoardViewHolder(view)
    }


    override fun onBindViewHolder(holder: DashBoardViewHolder, position: Int) {
//        val text = itemList[position]
//        holder.textView.text=text
        val bookItem=itemList[position]
        holder.bookname.text=bookItem.BookName
        holder.ratting.text=bookItem.Ratings
        holder.price.text=bookItem.Price
        holder.aurthorName.text=bookItem.Aurthor
//        holder.image.setImageResource(bookItem.Image)

//  Using the picasso implementation to fetch images from URL:

        Picasso.get().load(bookItem.Image).error(R.drawable.default_book_cover).into(holder.image)

        holder.lItem.setOnClickListener {
//            Toast.makeText(context,"${holder.bookname.text}",Toast.LENGTH_SHORT).show()

            val intent=Intent(context,DescriptionActivity::class.java)
            intent.putExtra("Id_book",bookItem.BookId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return  itemList.size
    }
}