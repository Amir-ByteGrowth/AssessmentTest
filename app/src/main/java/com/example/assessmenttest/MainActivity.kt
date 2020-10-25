package com.example.assessmenttest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.assessmenttest.adaptors.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {

    var tabPosition:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager!!.setAdapter(fragmentsAdapter())
        TabLayoutMediator(tabs!!, viewPager!!,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                if (position ==0){
                    tab.text = "Posts "
                    tabPosition=0
                }else{
                    tab.text = "Fav Posts "
                    tabPosition=1
                }

            }).attach()

    }

    override fun onBackPressed() {

        if (viewPager.currentItem ==1){
            viewPager.setCurrentItem(0)
        }else{
            super.onBackPressed()
        }
    }

    private fun fragmentsAdapter(): ViewPagerAdapter? {
        return ViewPagerAdapter(this)
    }


}