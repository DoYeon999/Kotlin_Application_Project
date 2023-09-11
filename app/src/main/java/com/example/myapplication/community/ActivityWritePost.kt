package com.example.myapplication.community

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.GeolocationPermissions
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.community.util.FileUtils
import com.example.myapplication.databinding.ActivityWritePostBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.util.FusedLocationSource
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.function.Consumer

class ActivityWritePost : AppCompatActivity() {
    private lateinit var mBinding: ActivityWritePostBinding
    private val db = FirebaseFirestore.getInstance()
    private var auth : FirebaseAuth? = null
    private var postId = ""
    private var replies: MutableList<Replies> = ArrayList()
    private var imageUriList = ArrayList<String>()
    private var favoritesList = mutableMapOf<String, Boolean>()
    private val bitmapList = ArrayList<Bitmap>()
    private val maxSize = 5
    private var addr = ""
    private lateinit var locationManager : LocationManager
    private lateinit var naverMap : NaverMap
    private val locationPermissionCode = 1001
    private val PERMISSION_REQUEST_CODE = 1
    private var lat : Double = 0.0
    private var lng : Double = 0.0
    private var imgcount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityWritePostBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val sharedPrefs = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nowUserNick = sharedPrefs.getString("nickname", "")
        findViewById<ImageView>(R.id.backbtn).setOnClickListener { finish() }
        findViewById<ImageView>(R.id.logomain).setOnClickListener {
            val intent = Intent(this@ActivityWritePost, MainActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.activitytitle).text = "글쓰기"
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")
        val signedup = sharedPref.getBoolean("signedup", false)
        if(signedup) {
            Log.d("writing", "**** $nick **** $url ****")
            findViewById<TextView>(R.id.loginbuttonmain).visibility = View.GONE
            findViewById<TextView>(R.id.toolbarnick).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.toolbarprofile).visibility = View.VISIBLE
            findViewById<TextView>(R.id.toolbarnick).text = nick
            if(url != "" && url != "null") {
                Glide.with(this)
                    .load(url)
                    .into(findViewById(R.id.toolbarprofile))
            }
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        requestLocationPermissions()
        startLocationUpdates()
        initVariable()
        setPostItem()
        onViewClick()
    }

    var mGetImage = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.data != null) {
            val data = result.data
            if (data!!.clipData != null) {
                val clipData = data.clipData
                Log.d("##INFO", "$clipData")
                //val uri = clipData?.getItemAt(0)?.uri
                for(i in 0 until(clipData?.itemCount!!)) {
                    if(i == maxSize) {
                        Toast.makeText(this@ActivityWritePost, "5장까지만 첨부가능합니다.", Toast.LENGTH_SHORT).show()
                        break
                    }
                    val uri = clipData?.getItemAt(i)?.uri
                    val bitmap : Bitmap = FileUtils.uriToBitmap(this@ActivityWritePost, uri)
                    bitmapList.add(bitmap)
                }
                //val bitmap: Bitmap =
                //    FileUtils.uriToBitmap(this@ActivityWritePost, uri)
                //bitmapList.add(bitmap)
                Log.i("##INFO", "(): bitmap.size = " + bitmapList.size)
                for(i in 0 until bitmapList.size) {
                    if(i == 0) mBinding.imOneWrite1.setImageBitmap(bitmapList.get(0))
                    else if(i == 1) mBinding.imOneWrite2.setImageBitmap(bitmapList.get(1))
                    else if(i == 2) mBinding.imOneWrite3.setImageBitmap(bitmapList.get(2))
                    else if(i == 3) mBinding.imOneWrite4.setImageBitmap(bitmapList.get(3))
                    else if(i == 4) mBinding.imOneWrite5.setImageBitmap(bitmapList.get(4))
                }

            }

        }
    }

    //region ---- getImages Section  ---
    var pickMultipleMedia = registerForActivityResult<PickVisualMediaRequest, List<Uri>>(
        ActivityResultContracts.PickMultipleVisualMedia(10)
    ) { uris: List<Uri> ->
        // photo picker.
        if (!uris.isEmpty()) {
            for (uri in uris) {
                Log.d("######", "uri : $uri")
                // 대용량 업그레이드 시 권한 길게 유지
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, flag)
                val bitmap: Bitmap =
                    FileUtils.uriToBitmap(this@ActivityWritePost, uri)
                //mBinding.imOneWrite.setImageBitmap(bitmap)
                bitmapList.add(bitmap)
            }
        } else {
            Log.d("#######", "uri 없음 !!")
        }
    }

    private val isPhotoPickerAvailable: Boolean
        private get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    fun launchPhotoPicker() {
        if (isPhotoPickerAvailable) {

            pickMultipleMedia.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(ImageOnly)
                    .build()
            )
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_PICK
            mGetImage.launch(intent)
        }
    }

    private fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10000000000, // Minimum time interval between location updates (in milliseconds)
                10f, // Minimum distance between location updates (in meters)
                locationListener
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            lat = location.latitude
            lng = location.longitude
            Log.d("lsy", "locationListener : ${lat}, ${lng}")
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(
                    this,
                    "Location permission denied.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //endregion
    private fun initVariable() {}
    private fun setPostItem() {
        val getPostData: PostDataModel? = intent.getSerializableExtra("postInfo") as PostDataModel?

        // 넘어온 데이터가 있을 경우
        if (getPostData != null) {
            mBinding.edFishspeciesWrite.setText(getPostData.fishspecies)
            mBinding.edContentWrite.setText(getPostData.content)
            //mBinding.edPasswordWrite.setText(getPostData.password)
            postId = getPostData.id

            //replies = getPostData.replies!!
            if (getPostData.pictures?.size == 0) return
            Glide.with(this).load(getPostData.pictures?.get(0)).into(mBinding.imOneWrite1)
            imageUriList = getPostData.pictures!!
            //Log.d("imgtest", "$imageUriList")
        }
    }

    private fun onViewClick() {
        mBinding.btCreateWrite.setOnClickListener { v ->
            mBinding.prLoadingPost.setVisibility(View.VISIBLE)
            //user 입력란에 공백이 있는지에 대한 확인
            val fishspecies: String = mBinding.edFishspeciesWrite.getText().toString()
            val content: String = mBinding.edContentWrite.getText().toString()
            //val password: String = mBinding.edPasswordWrite.getText().toString()
            if (fishspecies.isEmpty()) {
                Toast.makeText(this, "빈 부분이 있습니다", Toast.LENGTH_SHORT).show()
                mBinding.prLoadingPost.setVisibility(View.GONE)
            } else if (!bitmapList.isEmpty()) {
                bitmapList.forEach(Consumer { image: Bitmap ->
                    Log.i("##INFO", "onViewClick(): bitmapList is not empty")
                    getImageUri(image)
                })
            } else if (!imageUriList.isEmpty()) {
                addPost()
            } else {
                addPost()
            }
        }

        mBinding.ibtGetPhotoWrite.setOnClickListener { v ->
            if (mBinding.imOneWrite1.getDrawable() != null) {
                Toast.makeText(this@ActivityWritePost, "이미지는 최대 5장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                launchPhotoPicker()
            }
        }
        mBinding.imOneCancelWrite.setOnClickListener { v ->
            mBinding.imOneWrite1.setImageResource(0)
            mBinding.imOneWrite2.setImageResource(0)
            mBinding.imOneWrite3.setImageResource(0)
            mBinding.imOneWrite4.setImageResource(0)
            mBinding.imOneWrite5.setImageResource(0)
            if (!imageUriList.isEmpty()) {
                Log.i("##INFO", "onViewClick(): delete Image to imageUriList")
                val counter = imageUriList.size
                for(i in 0 until counter) imageUriList.removeAt(0)
            }
            if (!bitmapList.isEmpty()) {
                Log.i("##INFO", "onViewClick(): delete Image to bitmapList")
                val counter2 = bitmapList.size
                for(i in 0 until counter2) bitmapList.removeAt(0)
            }
        }
//        if (!bitmapList.isEmpty()) {
//            if (bitmapList.size == 2) {
//                bitmapList.removeAt(1)
//            } else {
//                bitmapList.removeAt(0)
//            }
//        }
    }


    private fun getImageUri(imageBitmap: Bitmap) {
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.getReference()
        val randomNum = (Math.random() * 100000).toInt()
        val mountainsRef: StorageReference =
            storageRef.child("communityImages/" + mBinding.edFishspeciesWrite.text.toString() + randomNum.toString() + ".jpg")
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask: UploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener(OnFailureListener { exception ->
            Log.i(
                "##INFO",
                "onFailure(): exception = " + exception.message
            )
        }).addOnSuccessListener(
            OnSuccessListener<Any?> {
                Log.i("##INFO", "onSuccess(): success save images")
                mountainsRef.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                    Log.d("##INFO", "${bitmapList.size}")
                    Log.i("##INFO", "onSuccess(): getImageUri = $uri")
                    Log.i("##INFO", "********${imageUriList.size}*******")
                    if (imageUriList.size <= 5) {
                        imageUriList.add(uri.toString())
                        Log.i("##INFO", "onSuccess(): bimLIst.size = " + bitmapList.size)
                    }
                    imgcount += 1
                    //addPost()
                    Log.d("##INFO", "$imgcount")
                    if(imgcount == bitmapList.size) {
                        addPost()
                        Toast.makeText(this@ActivityWritePost, "게시물 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        return@OnSuccessListener
                    }
                })
            })
    }

    private fun addPost() {
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val writerProfileImage = sharedPref.getString("profileuri", "")
        Log.d("writing" , "$writerProfileImage")
        val fishspecies: String = mBinding.edFishspeciesWrite.getText().toString()
        val content: String = mBinding.edContentWrite.getText().toString()
        //val password: String = mBinding.edPasswordWrite.getText().toString()
        auth = FirebaseAuth.getInstance()
        val nowuid = auth?.currentUser?.uid
        var nowUserNick = ""
        var res = false
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("qvyf49n4ce")
        //val location = locationSource.lastLocation
        //val latLng = LatLng(35.17511139898044, 129.17485747232317)
        //val latLng = LatLng(33.447950785763965, 134.8336172422697)
        var latLng = LatLng(lat, lng)
        //if(location != null) latLng = LatLng(location?.latitude!!, location?.longitude!!)
        //else latLng = LatLng(33.447950785763965, 134.8336172422697)
        val client = OkHttpClient()
        val apiKey = "qvyf49n4ce"
        val url = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=${latLng.longitude},${latLng.latitude}&sourcecrs=epsg:4326&output=json"

        val request = Request.Builder()
            .url(url)
            .header("X-NCP-APIGW-API-KEY-ID", apiKey)
            .header("X-NCP-APIGW-API-KEY", "iuQKWKRJphGXCsxwK3BTegMJzFejD0PxIb5ABxWD")
            .build()
        Log.d("maptest", "${request.url}")
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("maptest", "Error: ${e.message}")
            }
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                Log.d("maptest", "Address: $responseBody")
                val jsonObject = JSONObject(responseBody)

                val check = jsonObject.getJSONObject("status").get("code").toString() == "0"
                Log.d("maptest", "Address: $check")
                if(check) {
                    val address = jsonObject.getJSONArray("results").get(0).toString()
                    val addr0 = JSONObject(address).getJSONObject("region").getJSONObject("area0").get("name")
                    val addr1 = JSONObject(address).getJSONObject("region").getJSONObject("area1").get("name")
                    val addr2 = JSONObject(address).getJSONObject("region").getJSONObject("area2").get("name")
                    val addr3 = JSONObject(address).getJSONObject("region").getJSONObject("area3").get("name")
                    addr = String.format("%s %s %s", addr1.toString(), addr2.toString(), addr3.toString())
                    Log.d("maptest", "Address: $addr0 - $addr1 - $addr2 - $addr3")
                    Log.d("maptest", "###$addr###")
                } else {
                    Log.d("maptest", "###xxxxxxxxxx##")
                    addr = String.format("[%.2f, %.2f]", latLng.latitude, latLng.longitude)
                    Log.d("maptest", "바다 - [위도, 경도] : [${latLng.latitude}, ${latLng.longitude}]")
                    Log.d("maptest", "###$addr###")
                }

                val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
                val nowUserNick = sharedPref.getString("id", "")
                val nowUserProfile = sharedPref.getString("profileuri", "")

                db.collection("Users").document(nowuid.toString()).get().addOnSuccessListener {
                    Log.d("test1234", "$nowUserNick")
                    Log.d("test1234", "$addr")
                    //Log.d("test1234", "${it.data?.get("nickname")}")
                    res = PresenterPost.instance!!.setPost(
                        PostDataModel(
                            postId,
                            nowUserNick!!,
                            fishspecies,
                            content,
                            ArrayList(),
                            imageUriList,
                            0,
                            favoritesList,
                            addr,
                            writerProfileImage!!
                        ), postId
                    )
                    if (res) {
                        Toast.makeText(this@ActivityWritePost, "게시글 작성에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@ActivityWritePost, "게시글 작성에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

    }

}
