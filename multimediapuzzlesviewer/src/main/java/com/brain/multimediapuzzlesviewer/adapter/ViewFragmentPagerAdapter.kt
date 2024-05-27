package com.brain.multimediapuzzlesviewer.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewFragmentPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val fragmentTitleList: MutableList<String> = ArrayList()

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }

    fun addFragment(fragment: Fragment?, title: String?) {
        fragmentList.add(fragment!!)
        fragmentTitleList.add(title!!)
    }
}