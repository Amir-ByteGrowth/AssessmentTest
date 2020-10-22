package com.example.assessmenttest.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.assessmenttest.models.FavPosts

@Dao
interface FavDao {

    @Query("select * from FavPosts")
    fun getFavPostsLists(): LiveData<List<FavPosts>>

    @Query("SELECT * FROM FavPosts where id = :id ")
    suspend  fun getFavSinglePost(id : Int):FavPosts

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavPosts(favPost: FavPosts)

    @Delete
    fun delFavPost(favPostEntity: FavPosts)

}