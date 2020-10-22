package com.example.assessmenttest.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.assessmenttest.adaptors.CommentsAdaptor
import com.example.assessmenttest.models.Comments
import com.example.assessmenttest.models.FavPosts
import com.example.assessmenttest.repository.FavPostRep
import com.example.assessmenttest.repository.PostRepository
import com.example.assessmenttest.utils.AppResult
import com.example.assessmenttest.utils.LoadingState
import com.example.assessmenttest.utils.SingleLiveEvent
import com.example.assessmenttest.workmanager.FavPostWorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailViewModel(
    val repo: PostRepository,
    val favPostRep: FavPostRep,
    val commentsAdaptor: CommentsAdaptor
) :
    ViewModel(), Callback<List<Comments>> {
    //local variable
    var postId: MutableLiveData<Int> = MutableLiveData()
    var favUnFavDone: SingleLiveEvent<Boolean> = SingleLiveEvent()
    lateinit var commentsList: List<Comments>
    val showError = SingleLiveEvent<String>()

    val netWorkError = SingleLiveEvent<Boolean>()

    init {
        netWorkError.value = repo.isNetworkAvailable()
    }

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState


    private val _data = MutableLiveData<List<Comments>>()
    val data: LiveData<List<Comments>>
        get() = _data


    fun fetchComments() {
//        chkIsPostFav(1)
        _loadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repo.getAllComments(postId.value!!)

//            showLoading.value = false
            when (result) {
                is AppResult.Success -> {
                    _data.value = result.successData
                    commentsList = result.successData
//                     showError.value = null
                    _loadingState.postValue(LoadingState.LOADED)

                    if (commentsList != null && commentsList!!.isNotEmpty())
                        commentsAdaptor.updatePostItems(commentsList!!)
                }
                is AppResult.Error -> showError.value = result.exception.message
            }
        }
    }

    override fun onFailure(call: Call<List<Comments>>, t: Throwable) {
        _loadingState.postValue(LoadingState.error(t.message))
    }


    override fun onResponse(call: Call<List<Comments>>, response: Response<List<Comments>>) {
        if (response.isSuccessful) {
            _data.postValue(response.body())
            _loadingState.postValue(LoadingState.LOADED)
            commentsList = response.body()!!
            if (commentsList != null && commentsList.isNotEmpty())
                commentsAdaptor.updatePostItems(commentsList!!)
        } else {
            _loadingState.postValue(LoadingState.error(response.errorBody().toString()))
        }
    }


//    var favUnFavPost = SingleLiveEvent<Boolean>()


    fun makePostFav(favPosts: FavPosts) {
        favUnFavDone.value = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favPostRep.insertFavPosts(favPosts)
                favUnFavDone.postValue(true)
            }


        }

    }

    var tonight = SingleLiveEvent<FavPosts?>()

    fun initializeTonight() {
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): FavPosts? {
        var night = favPostRep.getPost(postId.value!!)
        return night
    }


    fun makeUnFavPost(favPosts: FavPosts) {
        favUnFavDone.value = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favPostRep.makeUnFavPost(favPosts)
                favUnFavDone.postValue(true)
            }
        }
    }




}