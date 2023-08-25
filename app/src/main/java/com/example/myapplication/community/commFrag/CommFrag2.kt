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

class CommFrag2 : Fragment() {


    private lateinit var binding : Commfrag2Binding
    var value : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Commfrag2Binding.inflate(inflater, container, false)
        Glide.with(this@CommFrag2).load(value).into(binding.commimg2)
        return binding.root
    }

    fun setUrl(url : String) {value = url}

}