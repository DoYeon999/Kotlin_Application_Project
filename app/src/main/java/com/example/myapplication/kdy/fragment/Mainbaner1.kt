package com.example.myapplication.kdy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityMainbaner1Binding

class Mainbaner1 : Fragment() {
    lateinit var binding: ActivityMainbaner1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMainbaner1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainbaner1Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }
}