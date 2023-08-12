package com.example.myapplication.FishingContent

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kotlin_application_project.R
import com.example.kotlin_application_project.databinding.ActivityFishingContentBinding
import com.example.myapplication.FishingContent.model.Poster
import com.example.myapplication.FishingContent.recycler.PhDividerItemDecoration
import com.example.myapplication.FishingContent.recycler.PosterAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

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
//        // 인텐트에서 글 정보 및 이미지 url을 가져옴
//        val title = intent.getStringExtra("title")
//        val imageUrl = intent.getStringExtra("imageUrl")
//        val imageView: ImageView =findViewById(R.id.myImageView)
//
//        imageView.setOnClickListener {
//            val dialog = Dialog(this)
//            dialog.setContentView(R.layout.dialog_image_preview)

//            val previewImage: ImageView = dialog.findViewById(R.id.previewImage)
//            previewImage.setImageResource(R.drawable.azz) // 확대해서 보여줄 이미지 설정

//            dialog.show()
//        }
//        val imageUrl = "여기에_파이어베이스_이미지_URL_입력"
//
//        val smallImageView: ImageView = findViewById(R.id.ps)
//        smallImageView.setOnClickListener {
//            val dialog = ImagePreviewDialog(this, imageUrl)
//            dialog.show()
//        }

    }
    private fun loadFirestoreData(path: String) {
        val firebaseStorage = FirebaseStorage.getInstance()
        val storageRef = firebaseStorage.reference
        var count = 0
        db.collection(path).get()
            .addOnSuccessListener {  querySnapshot  ->
                val dataToShow = StringBuilder()
                for (documentSnapshot in querySnapshot) {
                    if (documentSnapshot.exists()) {
                        val data = documentSnapshot.data
                        val title = data?.get("title") as String // 필드 이름을 적절히 변경하세요
                        val date = data?.get("date") as String
//                        val location = data?.get("location") as String
//                        val fs =data?.get("fs") as String
                        val ps = data?.get("ps") as String
                        storageRef.child(ps).downloadUrl.addOnSuccessListener {
                            uri -> posterlist.add(Poster(title, date, uri.toString()))
                            count++
                            if(count == querySnapshot.size()) {
                                viewBindingFunc(posterlist)
                            }
                        }

                        //dataToShow.append(info).append("\n")
                    }
                }
            }
            .addOnFailureListener { exception ->
                //binding.textView.text = "데이터를 불러오는 중에 오류가 발생했습니다."
                // 오류 처리 코드를 여기에 추가하세요


            }



    }
    fun viewBindingFunc(fishes : List<Poster>) {
        psAdapter = PosterAdapter(fishes)
//        val divideItemDecoration = DividerItemDecoration(binding.recyclerView.context,
//            LinearLayoutManager(this).orientation)
        Log.d("test", "${Color.GRAY}")
        val divideItemDecoration = PhDividerItemDecoration(10F, Color.GRAY)
        binding.recyclerView.adapter = psAdapter
        binding.recyclerView.addItemDecoration(divideItemDecoration)

    }

}