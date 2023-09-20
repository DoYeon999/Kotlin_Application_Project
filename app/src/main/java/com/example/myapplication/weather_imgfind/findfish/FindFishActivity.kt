package com.example.myapplication.weather_imgfind.findfish

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.MypageActivity
import com.example.myapplication.R
import com.example.myapplication.community.HomeActivity
import com.example.myapplication.databinding.ActivityFindFishBinding
import com.example.myapplication.kdy.LoginActivity
import com.example.myapplication.ml.Model
import com.example.myapplication.weather_imgfind.adapter.ConfidenceAdpater
import com.example.myapplication.weather_imgfind.weather.MapActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class FindFishActivity : AppCompatActivity() {
    var result: TextView? = null
    var confidence: TextView? = null
    var imageView: ImageView? = null
    var picture: Button? = null
    var imageSize = 224
    lateinit var binding : ActivityFindFishBinding
    lateinit var tmAdpater : ConfidenceAdpater
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindFishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findViewById<ImageView>(R.id.logomain).setOnClickListener{
            val intent = Intent(this@FindFishActivity, MainActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.loginbuttonmain).setOnClickListener {
            val intent = Intent(this@FindFishActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")
        val logincheck = sharedPref.getBoolean("signedup", false)
        if(logincheck) {
            findViewById<TextView>(R.id.toolbarnick).text = nick
            findViewById<TextView>(R.id.loginbuttonmain).visibility = View.GONE
            findViewById<TextView>(R.id.toolbarnick).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.toolbarprofile).visibility = View.VISIBLE
            if(url != "" && url != "null") {
                Glide.with(this)
                    .load(url)
                    .into(findViewById(R.id.toolbarprofile))
            }
        }
        findViewById<ImageView>(R.id.backbtn).setOnClickListener { finish() }
        findViewById<TextView>(R.id.activitytitle).text = "어종 검색"
        result = binding.result
        //confidence = binding.confidence
        imageView = binding.imageView

        //gallery request launcher..................
        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            try {
                var image = it!!.data!!.data!!
                var inputStream = contentResolver.openInputStream(image)
                val image2 = BitmapFactory.decodeStream(inputStream, null, null)
                val dimension = Math.min(image2!!.width, image2!!.height)
                var thumbnail = ThumbnailUtils.extractThumbnail(image2, dimension, dimension)
                imageView!!.setImageBitmap(image2)
                thumbnail = Bitmap.createScaledBitmap(image2, imageSize, imageSize, false)
                classifyImage(thumbnail)
                /*
                val calRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    //resources.getDimensionPixelSize(com.example.test10_12_jjh.R.dimen.imgSize),
                    //resources.getDimensionPixelSize(com.example.test10_12_jjh.R.dimen.imgSize)
                    224, 224
                )
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio
                //option.inSampleSize = imageSize

                var inputStream = contentResolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                inputStream = null
                Log.d("test16", "image convert start")
                bitmap?.let {
                    binding.imageView.setImageBitmap(bitmap)
                    classifyImage(bitmap)
                } ?: let{
                    Log.d("test16", "bitmap null")
                } */
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.cameraButton.setOnClickListener{
            // Launch camera if we have permission
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 1)
            } else {
                //Request camera permission if we don't have it.
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }

        findViewById<ImageView>(R.id.homepage).setOnClickListener{
            val intent = Intent(this@FindFishActivity, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.weatherpage).setOnClickListener{
            val intent = Intent(this@FindFishActivity, MapActivity::class.java)
            val checkdataloading = sharedPref.getBoolean("getdatabase", false)
            if(checkdataloading) startActivity(intent)
            else Toast.makeText(this@FindFishActivity, "데이터를 받아오는 중입니다!", Toast.LENGTH_LONG).show()
        }

        findViewById<ImageView>(R.id.cumunitypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@FindFishActivity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                binding.findfishlayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@FindFishActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.findfishlayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }

        findViewById<ImageView>(R.id.mypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@FindFishActivity, MypageActivity::class.java)
                startActivity(intent)
            } else {
                binding.findfishlayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@FindFishActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.findfishlayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }

        binding.galleryButton.setOnClickListener {
            Log.d("test16", "setOnClickListener")
            //gallery app........................
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

    }

    fun classifyImage(image: Bitmap?) {
        try {
            val model: Model = Model.newInstance(applicationContext)
            //Log.d("test16", "Classify")
            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            // get 1D array of 224 * 224 pixels in image
            val intValues = IntArray(imageSize * imageSize)
            image!!.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            var pixel = 0
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 255f))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)
            // Runs model inference and gets result.
            val outputs: Model.Outputs = model.process(inputFeature0)
            val outputFeature0: TensorBuffer = outputs.getOutputFeature0AsTensorBuffer()
            val confidences = outputFeature0.floatArray
            // find the index of the class with the biggest confidence.
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            //Log.d("test16", "Classify5")
            val classes = arrayOf("농어", "돌돔", "참돔", "감성돔", "벵에돔", "갈치", "우럭", "고등어", "광어", "망둥어", "전어", "삼치", "볼락", "도다리")
            result!!.text = classes[maxPos]
            var s = ""
            var strings = mutableListOf<String>()

            for (i in classes.indices) {
                strings.add(String.format("%s: %.1f%%" , classes[i], confidences[i] * 100))
                //s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100)
            }
            //confidence!!.text = s
            Log.d("test16", "$strings")
            binding.imageView.setImageBitmap(image)
            tmAdpater = ConfidenceAdpater(strings)
            binding.tmRecyclerView.adapter = tmAdpater
            binding.tmRecyclerView.layoutManager = LinearLayoutManager(this)
            Log.d("test16", "${tmAdpater.confidences}")
            tmAdpater.notifyDataSetChanged()
            // Releases model resources if no longer used.
            model.close()
        } catch (e: IOException) {
            Log.d("test16", "failed")
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            var image = data!!.extras!!["data"] as Bitmap?
            val dimension = Math.min(image!!.width, image.height)
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
            imageView!!.setImageBitmap(image)
            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
            classifyImage(image)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        // 비트맵 객체 그대로 사용하면, 사진 원본을 그대로 사용해서 메모리 부족 현상 생김
        // 그래서 옵션이라는 속성을 사용함
        val options = BitmapFactory.Options()
        // 실제 비트맵 객체를 생성하는 것 아니고, 옵션만 설정하겠다는 것을 의미
        options.inJustDecodeBounds = true
        try {
            // 실제 원본 사진의 물리 경로에 접근해서 바이트로 읽고
            // 사진을 읽은 바이트 단위
            var inputStream = contentResolver.openInputStream(fileUri)

            //inJustDecodeBounds 값을 true 로 설정한 상태에서 decodeXXX() 를 호출.
            //로딩 하고자 하는 이미지의 각종 정보가 options 에 설정 된다.
            BitmapFactory.decodeStream(inputStream, null, options)
            // 읽었던 원본의 사진의 메모리 사용을 반납하고,
            // 다시 해당 객체에 할당을 해제함 (null)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //비율 계산........................
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        // inSampleSize 비율 계산
        // height, width 원본의 가로 세로 크기
        // 이것보다 크면 원본의 사이즈를 반으로 줄여주는 작업을 진행함
        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}