package com.lukman.githubuser.Adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lukman.githubuser.Fragment.ListFolFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        val fragment = ListFolFragment()
        fragment.arguments = Bundle().apply {
            putInt(ListFolFragment.EXTRA_POSITION, position)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}