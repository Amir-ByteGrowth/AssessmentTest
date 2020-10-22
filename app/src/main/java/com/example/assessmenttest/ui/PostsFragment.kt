package com.example.assessmenttest.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.example.assessmenttest.R
import com.example.assessmenttest.utils.LoadingState
import com.example.assessmenttest.viewmodels.PostsViewModel
import com.example.assessmenttest.workmanager.FavPostWorkManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.posts_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

//import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("DEPRECATION")
class PostsFragment : Fragment() {

    companion object {
        fun newInstance() = PostsFragment()
    }

    //    private lateinit var viewModel: PostsViewModel
    private val userViewModel by viewModel<PostsViewModel>()

    lateinit var relPostParent: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.posts_fragment, container, false)
        relPostParent = view.findViewById(R.id.relPostParent)
        userViewModel.data.observe(activity!!, Observer {
            // Populate the UI
            if (it != null) {
                var mLayoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                recPosts!!.layoutManager = mLayoutManager
                recPosts.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        LinearLayoutManager.VERTICAL
                    )
                )

                recPosts!!.adapter = userViewModel.postsAdaptor

            }
        })
        userViewModel.moveToDetail.observe(activity!!, Observer {
            if (it != null) {
                startActivity(Intent(activity, PostDetailActivity::class.java).putExtra("Post", it))
            }
        })
        userViewModel.loadingState.observe(activity!!, Observer {
            // Observe the loading state
            if (it != null) {
                if (it == LoadingState.LOADING) {

                    if (userViewModel.netWorkError.value!!) {
                        val snack = Snackbar.make(
                            recPosts, resources.getString(R.string.loading),
                            Snackbar.LENGTH_LONG
                        )
                        snack.show()
                    } else {

                        val snack = Snackbar.make(
                            recPosts, resources.getString(R.string.network_error),
                            Snackbar.LENGTH_LONG
                        )
                        snack.show()
                    }
                    progressBar.visibility = View.VISIBLE
                } else if (it == LoadingState.LOADED) {
                    progressBar.visibility = View.GONE
                } else if (it == LoadingState.error("Data")) {
                    progressBar.visibility = View.GONE

                }
            }
        })

//        if (isOnline(context!!)) {
//            Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(context, "Not Connected", Toast.LENGTH_LONG).show()
//            val snack = Snackbar.make(
//                recPosts, resources.getString(R.string.network_error),
//                Snackbar.LENGTH_LONG
//            )
//            snack.show()
//        }

        startWorker()
        return view
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        // TODO: Use the ViewModel
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



        WorkManager.getInstance(activity!!)
            .enqueueUniqueWork("MakeFavPosts", ExistingWorkPolicy.KEEP, oneTimeRequest)
    }
}
