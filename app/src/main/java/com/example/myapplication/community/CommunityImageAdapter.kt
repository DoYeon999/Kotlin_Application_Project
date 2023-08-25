package com.example.myapplication.community

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.community.commFrag.CommFrag1
import com.example.myapplication.community.commFrag.CommFrag2
import com.example.myapplication.community.commFrag.CommFrag3
import com.example.myapplication.community.commFrag.CommFrag4
import com.example.myapplication.community.commFrag.CommFrag5
import com.example.myapplication.kdy.fragment.Fragment1

class CommunityImageAdapter(fragmentActivity: FragmentActivity, val commImgUrlList : ArrayList<String>) : FragmentStateAdapter(fragmentActivity) {

    var imgFragmentList : List<Fragment>

    init {
        imgFragmentList = listOf(CommFrag1(), CommFrag2(), CommFrag3(), CommFrag4(), CommFrag5())
    }

    override fun getItemCount(): Int = commImgUrlList.size

    override fun createFragment(position: Int): Fragment {
        Log.d("asdfghhhh", "${commImgUrlList[position]} --- ${position}")
        var nowFragment : Fragment =  imgFragmentList[position]
        if(position == 0) (nowFragment as CommFrag1).setUrl(commImgUrlList[position])
        if(position == 1) (nowFragment as CommFrag2).setUrl(commImgUrlList[position])
        if(position == 2) (nowFragment as CommFrag3).setUrl(commImgUrlList[position])
        if(position == 3) (nowFragment as CommFrag4).setUrl(commImgUrlList[position])
        if(position == 4) (nowFragment as CommFrag5).setUrl(commImgUrlList[position])
        return nowFragment
    }
}