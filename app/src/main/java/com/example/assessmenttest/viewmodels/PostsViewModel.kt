package com.example.assessmenttest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assessmenttest.adaptors.PostsAdaptor
import com.example.assessmenttest.models.Posts
import com.example.assessmenttest.repository.PostRepository
import com.example.assessmenttest.utils.AppResult
import com.example.assessmenttest.utils.LoadingState
import com.example.assessmenttest.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsViewModel(val repo: PostRepository, val postsAdaptor: PostsAdaptor) : ViewModel(),
    Callback<List<Posts>>, PostsAdaptor.ViewHolder.onItemClick {


    //local variable
    var moveToDetail: MutableLiveData<Posts> = MutableLiveData()
    lateinit var postList: MutableLiveData<List<Posts>>
    val showError = SingleLiveEvent<String>()

    val netWorkError = SingleLiveEvent<Boolean>()


    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState


    private val _data = MutableLiveData<List<Posts>>()
    val data: LiveData<List<Posts>>
        get() = _data

    init {
        moveToDetail.value = null
        postList= MutableLiveData()
        netWorkError.value=repo.isNetworkAvailable()
        fetchData()
    }

//    private fun fetchData() {
//
//        _loadingState.postValue(LoadingState.LOADING)
//            repo.getAllPosts()!!.enqueue(this)
//    }


    //    val showLoading = MutableLiveData<Boolean>()

    fun fetchData() {
//        showLoading.value = true
        _loadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repo.getAllCountries()

//            showLoading.value = false
            when (result) {
                is AppResult.Success -> {
                    _data.value=result.successData
                    postList.value = result.successData
                    showError.value = null
                    _loadingState.postValue(LoadingState.LOADED)

                    if (postList.value != null && postList.value!!.isNotEmpty())
                        postsAdaptor.updatePostItems(postList.value!!, this@PostsViewModel)
                }
                is AppResult.Error -> showError.value = result.exception.message
            }
        }
    }

    override fun onFailure(call: Call<List<Posts>>, t: Throwable) {
        _loadingState.postValue(LoadingState.error(t.message))
    }


    override fun onResponse(call: Call<List<Posts>>, response: Response<List<Posts>>) {
        if (response.isSuccessful) {
            _data.postValue(response.body())
            _loadingState.postValue(LoadingState.LOADED)
            postList.value = response.body()!!
            if (postList != null && postList.value!!.isNotEmpty())
                postsAdaptor.updatePostItems(postList.value!!, this)
        } else {
            _loadingState.postValue(LoadingState.error(response.errorBody().toString()))
        }
    }

    override fun onPostClick(position: Int) {
        moveToDetail.value = postList.value!![position]

    }
}