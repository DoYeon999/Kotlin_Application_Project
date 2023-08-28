package com.example.myapplication.kdy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityMainbaner2Binding

class Mainbaner2 : Fragment() {
    lateinit var binding: ActivityMainbaner2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMainbaner2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainbaner2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }
}