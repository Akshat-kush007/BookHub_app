package com.example.bookhub.database

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase


@Database(entities = [BookEntity ::class],version = 1)
abstract class BookDataBase : RoomDatabase() {

//  Creating bookDao function in order to use all methods of BookDao interface
    abstract fun bookDao():BookDao
}