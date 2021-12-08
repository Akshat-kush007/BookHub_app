package com.example.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.media.audiofx.Equalizer
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import com.example.bookhub.activity.MainActivity
import com.example.bookhub.adapterClass.DashboardRecyclerAdapter
import com.example.bookhub.models.Book
import com.example.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    lateinit var layout_progressBar:RelativeLayout
    lateinit var pregressBar:ProgressBar

//    using comparator Class of Kotlin to compare book ratings for sorting
    val ratingComparator=Comparator<Book>{book1, book2 ->
    if((book1.Ratings.compareTo(book2.Ratings, true))==0){
        book1.BookName.compareTo(book2.BookName, true)
    }else{
        book1.Ratings.compareTo(book2.Ratings, true)
    }
}

//    lateinit var btCheckConnectionButton: Button

//    val listItems= arrayListOf<String>(
//        "one book-1",
//        "one book-2",
//        "one book-3",
//        "one book-4",
//        "two book-1",
//        "two book-2",
//        "two book-3",
//        "two book-4",
//        "three book-1",
//        "last book"
//    )
//val bookInfoList = arrayListOf<Book>(
//    Book("P.S. I love You", "Cecelia Ahern", "Rs. 299", "4.5", R.drawable.ps_ily),
//    Book("The Great Gatsby", "F. Scott Fitzgerald", "Rs. 399", "4.1", R.drawable.great_gatsby),
//    Book("Anna Karenina", "Leo Tolstoy", "Rs. 199", "4.3", R.drawable.anna_kare),
//    Book("Madame Bovary", "Gustave Flaubert", "Rs. 500", "4.0", R.drawable.madame),
//    Book("War and Peace", "Leo Tolstoy", "Rs. 249", "4.8", R.drawable.war_and_peace),
//    Book("Lolita", "Vladimir Nabokov", "Rs. 349", "3.9", R.drawable.lolita),
//    Book("Middlemarch", "George Eliot", "Rs. 599", "4.2", R.drawable.middlemarch),
//    Book("The Adventures of Huckleberry Finn", "Mark Twain", "Rs. 699", "4.5", R.drawable.adventures_finn),
//    Book("Moby-Dick", "Herman Melville", "Rs. 499", "4.5", R.drawable.moby_dick),
//    Book("The Lord of the Rings", "J.R.R Tolkien", "Rs. 749", "5.0", R.drawable.lord_of_rings)
//)
val bookInfoList= arrayListOf<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_dashboard, container, false)

//  setting up menu (2)
        setHasOptionsMenu(true)

        val connectionManager=ConnectionManager()

        recyclerDashboard=view.findViewById(R.id.recyclerDashboard)

        layoutManager=LinearLayoutManager(activity)

        layout_progressBar=view.findViewById(R.id.layout_progressBar)
        pregressBar=view.findViewById(R.id.progressBar)

        layout_progressBar.visibility=View.VISIBLE

//        btCheckConnectionButton=view.findViewById(R.id.btCheckConnectionButton)
//
//
//
//        btCheckConnectionButton.setOnClickListener {
//
//            if (connectionManager.checkConnectin(activity as Context)) {
//                val dialog = AlertDialog.Builder(activity as Context)
//                dialog.setTitle("Succed")
//                dialog.setMessage("Intenet Connected")
//                dialog.setPositiveButton("OK") {text, listener->
//                //Positive code
//            }
//                dialog.setNegativeButton("Cancle"){text,listener->
////                    negetive here
//                }
//                dialog.create()
//                dialog.show()
//            }else{
//                val dialog = AlertDialog.Builder(activity as Context)
//                dialog.setTitle("Error")
//                dialog.setMessage("Intenet Not Connected")
//                dialog.setPositiveButton("OK") {text, listener->
//                    //Positive code
//                }
//                dialog.setNegativeButton("Cancle"){text,listener->
////                    negetive here
//                }
//                dialog.create()
//                dialog.show()
//            }
//        }



//        recyclerAdapter= DashboardRecyclerAdapter(activity  as Context,bookInfoList)
//
//        recyclerDashboard.layoutManager=layoutManager
//        recyclerDashboard.adapter=recyclerAdapter
//
//        recyclerDashboard.addItemDecoration(
//            DividerItemDecoration(recyclerDashboard.context,(layoutManager as LinearLayoutManager).orientation)
//        )

        val queue=Volley.newRequestQueue(activity as Context)

        val url="http://13.235.250.119/v1/book/fetch_books/"

        if (connectionManager.checkConnectin(activity as Context)) {


//  Creating the json Object---------------------------
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
//            println("The response is : ${it}")
//            parsing the json data in booklist


//  Getting response from json object--------------------------
                try{
                    layout_progressBar.visibility=View.GONE
                    val success = it.getBoolean("success")
                    if (success) {
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val bookJsonbject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonbject.getString("book_id"),
                                bookJsonbject.getString("name"),
                                bookJsonbject.getString("author"),
                                bookJsonbject.getString("rating"),
                                bookJsonbject.getString("price"),
                                bookJsonbject.getString("image")
                            )
                            bookInfoList.add(bookObject)
                        }



                        recyclerAdapter =
                            DashboardRecyclerAdapter(activity as Context, bookInfoList)

                        recyclerDashboard.layoutManager = layoutManager
                        recyclerDashboard.adapter = recyclerAdapter

//                        recyclerDashboard.addItemDecoration(
//                            DividerItemDecoration(
//                                recyclerDashboard.context,
//                                (layoutManager as LinearLayoutManager).orientation
//                            )
//                        )

                    } else {
                        Toast.makeText(activity as Context, "Error in fetching....", Toast.LENGTH_SHORT).show()
                    }
                }catch (e:JSONException){
                    Toast.makeText(activity as Context,"Somthing went wrong in fetching responce",Toast.LENGTH_SHORT).show()
                }




                }, Response.ErrorListener {

//                    Volly error handled here---------
                    if(activity!=null){
                    Toast.makeText(activity as Context,"Volly Error Occured",Toast.LENGTH_SHORT).show()
                    }

                }
                ) {
                    override fun getHeaders(): MutableMap<String, String> {

                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/java"
                        headers["token"] = "85e38cc8a10cb9"
                        return headers

                    }
                }
            queue.add(jsonObjectRequest)
        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Intenet Not Connected")
            dialog.setPositiveButton("Open Settings") {text, listener->

//   Implicite Intent -----------------------------------------------
            val settingsIntent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Close"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }

//    setting up menu item(2)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }


}