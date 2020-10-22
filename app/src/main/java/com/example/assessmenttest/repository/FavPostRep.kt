package com.example.assessmenttest.repository

import com.example.assessmenttest.models.FavPosts
import com.example.assessmenttest.room.FavDao

class FavPostRep(val favDao: FavDao) {

    fun getFavPosts() = favDao.getFavPostsLists()

   suspend fun getPost(id:Int) = favDao.getFavSinglePost(id)

    fun makeUnFavPost(favPosts: FavPosts)=favDao.delFavPost(favPosts)

    fun insertFavPosts(favPosts: FavPosts) = favDao.insertFavPosts(favPosts!!)
}