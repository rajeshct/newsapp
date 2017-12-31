package com.startup.news.localdatabase

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.startup.news.ApplicationInitializer
import com.startup.news.localdatabase.dataaccessobject.CategoryDataAccessObject
import com.startup.news.localdatabase.tables.CategoryModel

/**
 * Created by admin on 11/17/2017.
 */
@Database(entities = [(CategoryModel::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): CategoryDataAccessObject

    private object Holder {
        val INSTANCE = synchronized(AppDatabase::class.java) {
            Room.databaseBuilder(ApplicationInitializer.instance.applicationContext,
                    AppDatabase::class.java, "newsApp")
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }

    companion object {
        val instance: AppDatabase by lazy { Holder.INSTANCE }
    }


}