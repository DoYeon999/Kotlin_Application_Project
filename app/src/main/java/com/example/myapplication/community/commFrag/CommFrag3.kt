package com.example.myapplication.community.commFrag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.Commfrag1Binding
import com.example.myapplication.databinding.Commfrag2Binding
import com.example.myapplication.databinding.Commfrag3Binding


class CommFrag3 : Fragment() {


    private lateinit var binding : Commfrag3Binding
    var value : String = ""

    fun setUrl(url : String) {value = url}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Commfrag3Binding.inflate(inflater, container, false)
        Glide.with(this@CommFrag3).load(value).into(binding.commimg3)
        return binding.root
    }


}