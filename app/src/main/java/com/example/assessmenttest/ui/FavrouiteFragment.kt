package com.example.assessmenttest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assessmenttest.R
import com.example.assessmenttest.viewmodels.FavPostViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_post_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavrouiteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavrouiteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        retainInstance = true
    }

    private val userViewModel by viewModel<FavPostViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favrouite, container, false)
//        Toast.makeText(activity, "Fav Record Tab     ", Toast.LENGTH_SHORT)
//            .show()

        var recFavrouitePost = view.findViewById<RecyclerView>(R.id.recFavrouitePost)

        var mLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recFavrouitePost!!.layoutManager = mLayoutManager
        recFavrouitePost.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        recFavrouitePost!!.adapter = userViewModel.postsAdaptor

        userViewModel.postList.observe(activity!!, Observer {

            if (it != null && it!!.isNotEmpty()) {
                userViewModel.postsAdaptor.updatePostItems(it!!, this.userViewModel)
            } else {
                userViewModel.postsAdaptor.updatePostItems(listOf(),this.userViewModel)
                val snack = Snackbar.make(
                    recFavrouitePost, resources.getString(R.string.fav_post_error),
                    Snackbar.LENGTH_LONG
                )
                snack.show()

            }

        })

        userViewModel.data.observe(activity!!, Observer {
            Toast.makeText(activity, "Total Fav records are     " + it.size, Toast.LENGTH_SHORT)
                .show()
        })
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavrouiteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavrouiteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}