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
import com.example.myapplication.databinding.Commfrag4Binding

class CommFrag4 : Fragment() {


    private lateinit var binding : Commfrag4Binding
    var value : String = ""

    fun setUrl(url : String) {value = url}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Commfrag4Binding.inflate(inflater, container, false)
        Glide.with(this@CommFrag4).load(value).into(binding.commimg4)
        return binding.root
    }


}