package com.example.myapplication.kdy.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.kdy.fragment.Fragment1
import com.example.myapplication.kdy.fragment.Fragment2
import com.example.myapplication.kdy.fragment.Fragment3

class FishAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    val fragments: List<Fragment>

    init {
        fragments = listOf(Fragment1(), Fragment2(), Fragment3())
    }

    //Adapter가 가지고 있는 data set 안에서의 전체 아이템 수 리턴
    override fun getItemCount(): Int = fragments.size

    //특정 포지션에 연결된 새로운 Fragment를 제공하는 기능을 가진 메소드
    //override fun createFragment(position: Int): Fragment = fragments[position]

    override fun createFragment(position: Int): Fragment {
        val returnFragment : Fragment = fragments[position]
        return returnFragment
    }
}