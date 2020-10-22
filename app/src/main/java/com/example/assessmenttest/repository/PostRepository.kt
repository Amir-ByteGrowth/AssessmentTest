package com.example.assessmenttest.repository

import android.content.Context
import android.util.Log
import com.example.assessmenttest.apis.PostsApis
import com.example.assessmenttest.models.Comments
import com.example.assessmenttest.models.Posts
import com.example.assessmenttest.room.PostsDao
import com.example.assessmenttest.utils.AppResult
import com.example.assessmenttest.utils.Utility.Companion.isOnline
import com.example.assessmenttest.utils.handleApiError
import com.example.assessmenttest.utils.handleSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepository(private val api: PostsApis, val context: Context, val postsDao: PostsDao) {

//    fun getAllPosts(): Call<List<Posts>>? {
//
//
//        if (isOnline(context)) {
//            return api.getPosts()
//        }
//        return null
//    }

    fun isNetworkAvailable():Boolean{
        return isOnline(context)
    }
    suspend fun getAllCountries(): AppResult<List<Posts>> {
        if (isOnline(context)) {
            return try {
                val response = api.getPosts()
                if (response.isSuccessful) {
                    //save the data
                    response.body()?.let {
                        withContext(Dispatchers.IO) { postsDao.insertPosts(it) }
                    }
                    handleSuccess(response)
                } else {
                    handleApiError(response)
                }
            } catch (eexc: Exception) {
                AppResult.Error(eexc)
            }
        } else {
            //check in db if the data exists
            val data = getCountriesDataFromCache()
            return if (data.isNotEmpty()) {
                Log.d("DbData  ", "from db")
                AppResult.Success(data)
            } else
            //no network
                AppResult.Error(Exception("Net and data not available"))
        }
    }

    private suspend fun getCountriesDataFromCache(): List<Posts> {
        return withContext(Dispatchers.IO) {
            postsDao.getPostsLists()
        }
    }

    //    fun getAllComments(post_id: Int) = api.getComments(post_id.toString())
    suspend fun getAllComments(post_id: Int): AppResult<List<Comments>> {
        if (isOnline(context)) {
            return try {
                val response = api.getComments(post_id.toString())
                if (response.isSuccessful) {
                    //save the data
                    handleSuccess(response)
                } else {
                    handleApiError(response)
                }
            } catch (eexc: Exception) {
                AppResult.Error(eexc)
            }
        } else {


            //no network
            return AppResult.Error(Exception("Net and data not available"))
        }
    }


    suspend fun makPostFave(post: Posts){
         withContext(Dispatchers.IO) {
            postsDao.makePostFav(post)
        }
    }




}