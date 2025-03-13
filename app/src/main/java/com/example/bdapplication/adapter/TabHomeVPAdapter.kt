package com.example.bdapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bdapplication.adapter.fragment.ItemFragment
import com.example.bdapplication.adapter.fragment.MenuFragment

class TabHomeVPAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
       return when(position) {
           0 -> ItemFragment()
           1 -> MenuFragment()
           else -> ItemFragment()
       }
    }

}