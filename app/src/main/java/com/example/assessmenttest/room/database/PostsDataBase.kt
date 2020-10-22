package com.example.assessmenttest.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assessmenttest.models.FavPosts
import com.example.assessmenttest.models.Posts
import com.example.assessmenttest.room.FavDao
import com.example.assessmenttest.room.PostsDao

@Database(entities = [Posts::class, FavPosts::class], version = 1, exportSchema = false)
abstract class PostsDataBase : RoomDatabase() {

    abstract fun postDao(): PostsDao
    abstract fun favPostDao(): FavDao

    companion object {

        @Volatile
        private var INSTANCE: PostsDataBase? = null

        fun getDatabase(context: Context): PostsDataBase? {
            if (INSTANCE == null) {
                synchronized(PostsDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            PostsDataBase::class.java, "post_db"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}