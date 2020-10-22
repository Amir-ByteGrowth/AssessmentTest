package com.example.assessmenttest.apis

import com.example.assessmenttest.models.Comments
import com.example.assessmenttest.models.Posts
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsApis {
//    @GET("posts")
//    fun  getPosts(): Call<List<Posts>>

    @GET("posts")
    suspend fun getPosts(): Response<List<Posts>>

//    @GET("/posts/{post_id}/comments")
//    fun getComments(@Path("post_id") post_id: String): Call<List<Comments>>

    @GET("/posts/{post_id}/comments")
   suspend fun getComments(@Path("post_id") post_id: String): Response<List<Comments>>
}