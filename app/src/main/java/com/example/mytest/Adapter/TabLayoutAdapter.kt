package com.example.mytest.Adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mytest.VideoFragmet
import com.example.mytest.FeedFragment

class TabLayoutAdapter(
    var mContext: Context,
    fragmentManager: FragmentManager?,
    var mTotalTabs: Int
) : FragmentPagerAdapter(
    fragmentManager!!
) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> VideoFragmet()
            1 -> FeedFragment()
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return mTotalTabs
    }
}