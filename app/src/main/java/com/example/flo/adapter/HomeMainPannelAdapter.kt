package com.example.flo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.fragment.HomeMainPannelFragment

class HomeMainPannelAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {

    var fragmentList : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size -1)
    }

    fun removeFragment(){
        fragmentList.removeLast()
        notifyItemRemoved(fragmentList.size -1)
    }

}