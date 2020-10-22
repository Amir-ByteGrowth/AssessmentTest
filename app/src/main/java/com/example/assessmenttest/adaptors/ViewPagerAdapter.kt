package com.example.assessmenttest.adaptors

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.assessmenttest.ui.FavrouiteFragment
import com.example.assessmenttest.ui.PostsFragment


class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return PostsFragment.newInstance()
            1 -> return FavrouiteFragment.newInstance("", "")

        }
        return FavrouiteFragment.newInstance("", "")
    }

    override fun getItemCount(): Int {
        return CARD_ITEM_SIZE
    }

    companion object {
        private const val CARD_ITEM_SIZE = 2
    }
}