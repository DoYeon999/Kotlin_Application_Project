package com.example.myapplication.FishingContent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.FishingContent.model.FishContest
import com.example.myapplication.databinding.ActivityFishingContentBinding
import com.example.myapplication.FishingContent.model.Poster
import com.example.myapplication.FishingContent.recycler.PosterAdapter
import com.example.myapplication.weather_imgfind.net.APIApplication
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FishingContent : AppCompatActivity() {


    private lateinit var binding: ActivityFishingContentBinding
    private var posterlist= mutableListOf<Poster>()
    private val db = FirebaseFirestore.getInstance()
    lateinit var psAdapter : PosterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishingContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button2.setOnClickListener{
            posterlist.clear()
            loadFirestoreData("포스터")
        }

    }



    private fun loadFirestoreData(path: String) {
        val fishService = (applicationContext as APIApplication).fishService
        val contest = fishService.fishContestList()
        contest.enqueue(object : Callback<List<FishContest>> {
            override fun onResponse(call: Call<List<FishContest>>, response: Response<List<FishContest>>) {
                val contestdata = response.body()
                viewBindingFunc(contestdata!!)
            }
            override fun onFailure(call: Call<List<FishContest>>, t: Throwable) {
            }
        })

    }
    fun viewBindingFunc(fishes : List<FishContest>) {
        psAdapter = PosterAdapter(fishes)
        binding.recyclerView.adapter = psAdapter

    }

}