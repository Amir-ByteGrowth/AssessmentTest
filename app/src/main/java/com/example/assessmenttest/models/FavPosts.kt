package com.example.assessmenttest.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "FavPosts")
data class FavPosts(
    val userId: Int,
    @PrimaryKey
    val id: Int,
    val title: String,
    val body: String
) : Serializable