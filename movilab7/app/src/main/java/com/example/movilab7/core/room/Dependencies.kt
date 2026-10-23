package com.example.movilab7.core

import android.content.Context
import androidx.room.Room

class DatabaseProvider(context: Context) {

    val database = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "uvg.db"
    ).build()
}