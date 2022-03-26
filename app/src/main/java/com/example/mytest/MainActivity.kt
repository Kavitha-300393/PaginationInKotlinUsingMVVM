package com.example.mytest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mytest.R
import com.example.mytest.Adapter.TabLayoutAdapter
import com.example.mytest.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding ;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.setContent(this)
        binding.tabs.addTab(binding.tabs.newTab().setText("Video"))
        binding.tabs.addTab(binding.tabs.newTab().setText("Feed"))
        val adapter = TabLayoutAdapter(this, supportFragmentManager, binding.tabs.tabCount)
        binding.frameLayout.adapter = adapter
        binding.frameLayout.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding.tabs))
        binding.tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.frameLayout.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}