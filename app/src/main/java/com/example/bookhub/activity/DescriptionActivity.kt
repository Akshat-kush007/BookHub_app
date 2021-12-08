package com.example.bookhub.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import com.example.bookhub.database.BookDataBase
import com.example.bookhub.database.BookEntity
import com.example.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var imgBookImage:ImageView
    lateinit var txtBookName:TextView
    lateinit var txtBookAurthor:TextView
    lateinit var txtBookPrice:TextView
    lateinit var txtBookRatings:TextView
    lateinit var txtAuthorName:TextView
    lateinit var txtBookDescription:TextView
    lateinit var btFaviourite:Button
    lateinit var pregressLayout:RelativeLayout
    lateinit var progressBar: ProgressBar


    var bookId:String="100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        toolbar=findViewById(R.id.toolbar)
        imgBookImage=findViewById(R.id.imgBookImage)
        txtBookName=findViewById(R.id.txtBookName)
        txtBookAurthor=findViewById(R.id.txtBookAurthor)
        txtBookPrice=findViewById(R.id.txtBookPrice)
        txtBookRatings=findViewById(R.id.txtBookRatings)
        txtAuthorName=findViewById(R.id.txtBookAuthorName)
        txtBookDescription=findViewById(R.id.txtBookDescription)
        btFaviourite=findViewById(R.id.btFaviourite)
        pregressLayout=findViewById(R.id.pregressLayout)
        progressBar=findViewById(R.id.progressBar)


//setting progress layout visible as long as content load...
        pregressLayout.visibility= View.VISIBLE
        progressBar.visibility= View.VISIBLE

//setuping toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"


        if (intent!=null){
            bookId= intent.getStringExtra("Id_book").toString()
        }else{
            finish()
            Toast.makeText(this@DescriptionActivity,"Something went wrong 1",Toast.LENGTH_SHORT ).show()
        }

        if (bookId=="100"){
            finish()
            Toast.makeText(this@DescriptionActivity,"Something went wrong 2",Toast.LENGTH_SHORT ).show()
        }

//  Creating get post---------------------------------------------------------------------------------
        val queue=Volley.newRequestQueue(this@DescriptionActivity)
        val url="http://13.235.250.119/v1/book/get_book/"
//  Creating the json Object---- as jronParams--------to sent data over Post request
        val jsonParams=JSONObject()
        jsonParams.put("book_id",bookId)


//        cheacking connection of device before making the request-----------------------------
        if(ConnectionManager().checkConnectin(this@DescriptionActivity)){

            val jsonObjectRequest =
                object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {


                    try {
                        val success=it.getBoolean("success")
                        if (success){
                            pregressLayout.visibility=View.GONE
                            progressBar.visibility=View.GONE
                            val bookObject=it.getJSONObject( "book_data")

                            val bookUrl=bookObject.getString("image")

                            Picasso.get().load(bookObject.getString("image")).error(R.drawable.default_book_cover).into(imgBookImage)
                            txtBookName.text=bookObject.getString("name")
                            txtBookAurthor.text=bookObject.getString("author")
                            txtBookPrice.text=bookObject.getString("price")
                            txtBookRatings.text=bookObject.getString("rating")
                            txtBookDescription.text=bookObject.getString("description")


//              using the AsyncsClass
//                  Creating the book entity object-----------(for parameter)
                            val bookEntity=BookEntity(
                            bookId?.toInt() as Int,
                            txtBookName.text.toString(),
                            txtBookAurthor.text.toString(),
                            txtBookPrice.text.toString(),
                            txtBookRatings.text.toString(),
                            txtBookDescription.text.toString(),
                            bookUrl
                            )
//                  creating the DBsyncTask object in mode 1 to check the existance of book in favourites
                            val checkFav=DBsyncTask(applicationContext,bookEntity,1).execute()
                            val isFav=checkFav.get()

                            if(isFav){
                                btFaviourite.text="Remove to Favourite"
                                val favColor=ContextCompat.getColor(applicationContext,R.color.favourite)
                                btFaviourite.setBackgroundColor(favColor)
                            }
                            else{
                                btFaviourite.text="Add to Favourite"
                                val noFavColor=ContextCompat.getColor(applicationContext,R.color.nofavourite)
                                btFaviourite.setBackgroundColor(noFavColor)
                            }

                            btFaviourite.setOnClickListener {
                                if(!DBsyncTask(applicationContext,bookEntity,1).execute().get()){

                                    val async=DBsyncTask(applicationContext,bookEntity,2).execute()
                                    val result=async.get()
                                    if(result){
                                        btFaviourite.text="Remove to Favourite"
                                        val favColor=ContextCompat.getColor(applicationContext,R.color.favourite)
                                        btFaviourite.setBackgroundColor(favColor)
                                        Toast.makeText(this@DescriptionActivity,"Book Added To Favourites",Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        Toast.makeText(this@DescriptionActivity,"Some Error Occured",Toast.LENGTH_SHORT).show()
                                    }

                                }
                                else{
                                    val async=DBsyncTask(applicationContext,bookEntity,3).execute()
                                    val result=async.get()
                                    if(result){
                                        btFaviourite.text="Add to Favourite"
                                        val noFavColor=ContextCompat.getColor(applicationContext,R.color.nofavourite)
                                        btFaviourite.setBackgroundColor(noFavColor)
                                        Toast.makeText(this@DescriptionActivity,"Book Removed From Favourites",Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        Toast.makeText(this@DescriptionActivity,"Some Error Occured",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                        }else{
                            Toast.makeText(this@DescriptionActivity,"Something went wrong 3",Toast.LENGTH_SHORT ).show()
                        }
                    }catch (e:Exception){
                        Toast.makeText(this@DescriptionActivity,"Something went wrong 4",Toast.LENGTH_SHORT ).show()

                    }

                },Response.ErrorListener {
                    Toast.makeText(this@DescriptionActivity,"Something went wrong 5",Toast.LENGTH_SHORT ).show()

                }
                ){
                    override fun getHeaders(): MutableMap<String, String> {

                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/java"
                        headers["token"] = "85e38cc8a10cb9"
                        return headers

                    }
                }
            queue.add(jsonObjectRequest)
//    post request finishes here-----------------------------------------------------------------------------------------------

        }else{
//            Alert dialoge box---------------------------------------
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Intenet Not Connected")
            dialog.setPositiveButton("Open Settings") {text, listener->

//   Implicite Intent -----------------------------------------------
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                this@DescriptionActivity?.finish()
            }
            dialog.setNegativeButton("Close"){text,listener->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }


    }
//creating class of type AsyncsTask<parms,progress,resutl type)
//    to creat a bsckgroung thread (worker thread)

    class DBsyncTask(val context: Context,val bookEntity: BookEntity,val mode:Int): AsyncTask<Void,Void,Boolean>(){

// Initilisation of BookDataBase class in ogder to use the functionality od dao interface
        val db= Room.databaseBuilder(context,BookDataBase::class.java,"books").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){
                1->{
//                    check book in db
                    val book :BookEntity?=db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book !=null
                }
                2->{
//                    add book in db
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3->{
//                    remove book from db
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }

            return false
        }

    }

}