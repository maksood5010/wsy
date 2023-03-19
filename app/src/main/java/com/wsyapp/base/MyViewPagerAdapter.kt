package com.wsyapp.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

open class MyViewPagerAdapter(framenrMgr: FragmentManager) : FragmentStatePagerAdapter(framenrMgr) {

    var list = mutableListOf<Fragment>()
    fun addFragment(fragment: Fragment) {
        list.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return list.get(position)
    }

    override fun getCount(): Int {
        if (list != null) return list.size
        return 0
    }

}