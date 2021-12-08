package com.example.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface BookDao {
    @Insert
    fun insertBook(bookEntity: BookEntity)

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    @Query("SELECT * FROM books")
    fun getAllBook() : List<BookEntity>

// note:- here ":" specify that the value of this parameter is come from function mentioned just below
    @Query("SELECT * FROM books WHERE book_id= :bookId")
    fun getBookById(bookId :String): BookEntity
}