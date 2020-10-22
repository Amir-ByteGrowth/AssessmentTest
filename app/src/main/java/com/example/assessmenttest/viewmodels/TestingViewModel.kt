package com.example.assessmenttest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assessmenttest.adaptors.CommentsAdaptor
import com.example.assessmenttest.models.Comments
import com.example.assessmenttest.repository.PostRepository
import com.example.assessmenttest.utils.LoadingState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestingViewModel(val repository: PostRepository,adaptor: CommentsAdaptor) : ViewModel(),
    Callback<List<Comments>> {
    val books: MutableLiveData<Boolean> = MutableLiveData()

    //local variable
    var postId: MutableLiveData<Int> = MutableLiveData()
    var moveToDetail: MutableLiveData<Comments> = MutableLiveData()
    lateinit var commentsList :List<Comments>


    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState


    private val _data = MutableLiveData<List<Comments>>()
    val data: LiveData<List<Comments>>
        get() = _data

    init {
        books.value = true
        moveToDetail.value=null
        fetchComments()
    }
    private fun fetchComments() {
        _loadingState.postValue(LoadingState.LOADING)
//        repository.getAllComments(1).enqueue(this)
    }
    override fun onFailure(call: Call<List<Comments>>, t: Throwable) {

    }

    override fun onResponse(call: Call<List<Comments>>, response: Response<List<Comments>>) {

    }
}