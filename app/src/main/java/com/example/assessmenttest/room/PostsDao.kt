package com.example.assessmenttest.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.assessmenttest.models.Posts

@Dao
interface PostsDao {

    @Query("select * from posts")
    fun getPostsLists(): List<Posts>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(users: List<Posts>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun makePostFav(posts: Posts)

    @Query("select * from posts where fav = 1")
    fun getFavPosts(): LiveData<List<Posts>>

}