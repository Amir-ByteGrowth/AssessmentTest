package com.example.assessmenttest.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.example.assessmenttest.R
import com.example.assessmenttest.models.FavPosts
import com.example.assessmenttest.models.Posts
import com.example.assessmenttest.utils.LoadingState
import com.example.assessmenttest.utils.Utility
import com.example.assessmenttest.viewmodels.PostDetailViewModel
import com.example.assessmenttest.workmanager.FavPostWorkManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_post_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

//import org.koin.androidx.viewmodel.ext.android.viewModel

//import kotlinx.android.synthetic.main.posts_fragment.*
//import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailActivity : AppCompatActivity() {

//    private val postDetailViewModel by viewModel<PostDetailViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)


        val postEntity = intent.getSerializableExtra("Post") as? Posts
        val postDetailViewModel by viewModel<PostDetailViewModel>()
        postDetailViewModel.postId.value = postEntity!!.id


        imgBack.setOnClickListener(View.OnClickListener {
            finish()

        })



        imgFav.setOnClickListener(View.OnClickListener {
            if (!Utility.isOnline(applicationContext)) {
                startWorker()
            }
            if (postEntity.fav == 0) {

                postDetailViewModel.makePostFav(
                    FavPosts(
                        postEntity!!.userId,
                        postEntity!!.id,
                        postEntity!!.title,
                        postEntity!!.body
                    )
                )

            } else
                if (postEntity.fav == 1) {

                    postDetailViewModel.makeUnFavPost(
                        FavPosts(
                            postEntity!!.userId,
                            postEntity!!.id,
                            postEntity!!.title,
                            postEntity!!.body
                        )
                    )

                }


        })
        tvTitle.text = postEntity!!.title
        tvBody.text = postEntity!!.body


        postDetailViewModel.fetchComments()
        postDetailViewModel.initializeTonight()
        postDetailViewModel.tonight.observe(this, Observer {
            if (it == null) {
                imgFav.setBackgroundResource(R.drawable.black_heart_icon)
                postEntity.fav = 0
//                tvBody.setText("null")
            } else {
                imgFav.setBackgroundResource(R.drawable.red_heart_icon)
                postEntity.fav = 1
//                tvBody.setText("null" + it.id)
            }
        })

        postDetailViewModel.favUnFavDone.observe(this, Observer {
            if (it) {
                postDetailViewModel.initializeTonight()
            }
        })



        postDetailViewModel.data.observe(PostDetailActivity@ this, Observer {
            // Populate the UI
            if (it != null) {
                var mLayoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                recComments!!.layoutManager = mLayoutManager
                recComments.addItemDecoration(
                    DividerItemDecoration(
                        this,
                        LinearLayoutManager.VERTICAL
                    )
                )
                recComments!!.adapter = postDetailViewModel.commentsAdaptor

            }
        })

        postDetailViewModel.loadingState.observe(this!!, Observer {
            // Observe the loading state
            if (it != null) {
                if (it == LoadingState.LOADING) {

                    if (postDetailViewModel.netWorkError.value!!) {
                        val snack = Snackbar.make(
                            recComments, resources.getString(R.string.loading),
                            Snackbar.LENGTH_LONG
                        )
                        snack.show()
                    } else {

                        val snack = Snackbar.make(
                            recComments, resources.getString(R.string.network_error),
                            Snackbar.LENGTH_LONG
                        )
                        snack.show()
                    }
                    progressBarcomments.visibility = View.VISIBLE
                } else if (it == LoadingState.LOADED) {
                    progressBarcomments.visibility = View.GONE
                } else if (it == LoadingState.error("Data")) {
                    progressBarcomments.visibility = View.GONE

                }
            }
        })


    }

    fun startWorker() {
        val data = Data.Builder()
            .putString("blank", "")
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)

        val oneTimeRequest = OneTimeWorkRequest.Builder(FavPostWorkManager::class.java)
            .setInputData(data)
            .setConstraints(constraints.build())
            .addTag("Posts")
            .build()



        WorkManager.getInstance(applicationContext!!)
            .enqueueUniqueWork("MakeFavPosts", ExistingWorkPolicy.KEEP, oneTimeRequest)
    }

}