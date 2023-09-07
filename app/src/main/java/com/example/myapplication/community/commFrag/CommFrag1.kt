package com.example.myapplication.community.commFrag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.Commfrag1Binding
import com.example.myapplication.databinding.Fragment1Binding
import kotlinx.coroutines.NonDisposableHandle.parent

class CommFrag1() : Fragment() {


    private lateinit var binding : Commfrag1Binding
    var value : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Commfrag1Binding.inflate(inflater, container, false)
        //Log.d("asdfghhhh", "******************* $value")
        //binding.commimg1.setImageResource(R.drawable.blackgallery)
        //binding.txttxt.text = "qwerty"
        Glide.with(this@CommFrag1).load(value).into(binding.commimg1)
        return binding.root
    }

 //   override fun onCreate(savedInstanceState: Bundle?) {
//        binding = Commfrag1Binding.inflate(layoutInflater)
//        super.onCreate(savedInstanceState)
//        Log.d("asdfghhhh", "******************* $value")
//        binding.commimg1.setImageResource(R.drawable.blackgallery)
//        binding.txttxt.text = "qwerty"
        //Glide.with(this@CommFrag1).load(value).into(binding.commimg1)
 //   }

    fun setUrl(url : String) {value = url}

}