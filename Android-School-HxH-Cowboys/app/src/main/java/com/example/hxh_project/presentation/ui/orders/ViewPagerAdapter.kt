package com.example.hxh_project.presentation.ui.orders

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentList = mutableListOf<Fragment>()

    fun addFragment(fragment: Fragment) = fragmentList.add(fragment)

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemCount() = fragmentList.size
}