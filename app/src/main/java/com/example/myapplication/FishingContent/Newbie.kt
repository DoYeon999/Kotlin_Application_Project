package com.example.myapplication.FishingContent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlin_application_project.databinding.ActivityNewbieBinding
import com.example.myapplication.FishingContent.model.Guide
import com.example.myapplication.FishingContent.recycler.NewbieAdpater
import com.google.firebase.firestore.FirebaseFirestore

class Newbie : AppCompatActivity() {
    private lateinit var binding: ActivityNewbieBinding
    private var newbielist = mutableListOf<Guide>()

    private var db : FirebaseFirestore? = null
    //private val collectionReference = db.collection("매듭") // 컬렉션 이름을 적절히 변경하세요
    lateinit var nbAdapter : NewbieAdpater


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        binding = ActivityNewbieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            newbielist.clear()
            loadFirestoreData("매듭")  // 원하는 문서 ID를 적절히 변경하세요
        }

        binding.button1.setOnClickListener {
            newbielist.clear()
            loadFirestoreData("미끼")  // 원하는 문서 ID를 적절히 변경하세요
        }
    }

    private fun loadFirestoreData(path: String) {
        db?.collection(path)?.get()
            ?.addOnSuccessListener {  querySnapshot  ->
                val dataToShow = StringBuilder()
                for (documentSnapshot in querySnapshot) {
                    if (documentSnapshot.exists()) {
                        val data = documentSnapshot.data
                        val title = data?.get("title") as String // 필드 이름을 적절히 변경하세요
                        val url = data?.get("url") as String
                        val thumbnail = data?.get("thumbnail") as String
                        Log.d("tt", "$title, $url, $thumbnail")
                        newbielist.add(Guide(title, url, thumbnail))
                        //dataToShow.append(info).append("\n")
                    }
                }

                viewBindingFunc(newbielist)


                /*
                if (dataToShow.isNotEmpty()) {
                    binding.textView.text = dataToShow.toString()
                } else {
                    binding.textView.text = "데이터가 없습니다."
                }*/
            }
            ?.addOnFailureListener { exception ->
                //binding.textView.text = "데이터를 불러오는 중에 오류가 발생했습니다."
                // 오류 처리 코드를 여기에 추가하세요
            }

    }
    fun viewBindingFunc(newbies : List<Guide>) {
        Log.d("tt", "=====$newbies=====")
        nbAdapter = NewbieAdpater(newbies)
        Log.d("tt", "=====${nbAdapter.guides.toString()}=====")
        binding.recyclerView2.adapter = nbAdapter
//        val binding = ActivityNewbieBinding.inflate(layoutInflater)
//        val recycle  = binding.recyclerView2

    }
}
