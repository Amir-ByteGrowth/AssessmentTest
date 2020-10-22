package com.example.assessmenttest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assessmenttest.adaptors.FavPostAdaptor
import com.example.assessmenttest.models.FavPosts
import com.example.assessmenttest.repository.FavPostRep
import com.example.assessmenttest.utils.LoadingState
import com.example.assessmenttest.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class FavPostViewModel(val repo: FavPostRep, val postsAdaptor: FavPostAdaptor) : ViewModel(),
    FavPostAdaptor.ViewHolder.onItemClick {


    //local variable
    var moveToDetail: MutableLiveData<FavPosts> = MutableLiveData()
    lateinit var postList: LiveData<List<FavPosts>>
    val showError = SingleLiveEvent<String>()


    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState


    private val _data = MutableLiveData<List<FavPosts>>()
    val data: LiveData<List<FavPosts>>
        get() = _data

    init {
        moveToDetail.value = null

        fetchData()
    }


    fun fetchData() {

        _loadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            postList = repo.getFavPosts()
            if (postList != null) {
//                _data.value = postList.value!!
                if (postList.value != null && postList.value!!.isNotEmpty())
                    postsAdaptor.updatePostItems(postList.value!!, this@FavPostViewModel)
            }

        }
    }

    override fun onPostClick(position: Int) {
        moveToDetail.value = postList.value!![position]

    }
}