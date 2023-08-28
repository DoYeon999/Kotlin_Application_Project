package com.example.myapplication.kdy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityMainbaner3Binding

class Mainbaner3 : Fragment() {
    lateinit var binding: ActivityMainbaner3Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMainbaner3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainbaner3Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }
}