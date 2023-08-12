package com.example.myapplication.community

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.community.util.FileUtils
import com.example.myapplication.databinding.ActivityWritePostBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.function.Consumer

class ActivityWritePost : AppCompatActivity() {
    private lateinit var mBinding: ActivityWritePostBinding
    private val db = FirebaseFirestore.getInstance()
    private var auth : FirebaseAuth? = null
    private var postId = ""
    private var replies: ArrayList<Replies> = ArrayList<Replies>()
    private var imageUriList = ArrayList<String>()
    private var favoritesList = mutableMapOf<String, Boolean>()
    private val bitmapList = ArrayList<Bitmap>()
    private val maxSize = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityWritePostBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
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
                val uri = clipData?.getItemAt(0)?.uri
                val bitmap: Bitmap =
                    FileUtils.uriToBitmap(this@ActivityWritePost, uri)
                bitmapList.add(bitmap)
                Log.i("##INFO", "(): bitmap.size = " + bitmapList.size)
                mBinding.imOneWrite.setImageBitmap(bitmap)
            }

        }
    }

    //region ---- getImages Section  ---
    var pickMultipleMedia = registerForActivityResult<PickVisualMediaRequest, List<Uri>>(
        ActivityResultContracts.PickMultipleVisualMedia(maxSize)
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
                mBinding.imOneWrite.setImageBitmap(bitmap)
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

    //endregion
    private fun initVariable() {}
    private fun setPostItem() {
        val getPostData: PostDataModel? = intent.getSerializableExtra("postInfo") as PostDataModel?

        // 넘어온 데이터가 있을 경우
        if (getPostData != null) {
            mBinding.edFishspeciesWrite.setText(getPostData.fishspecies)
            mBinding.edContentWrite.setText(getPostData.content)
            mBinding.edPasswordWrite.setText(getPostData.password)
            postId = getPostData.id
            replies = getPostData.replies!!
            if (getPostData.pictures?.size == 0) return

            Glide.with(this).load(getPostData.pictures?.get(0)).into(mBinding.imOneWrite)
            imageUriList = getPostData.pictures!!
        }
    }

    private fun onViewClick() {
        mBinding.btCreateWrite.setOnClickListener { v ->
            mBinding.prLoadingPost.setVisibility(View.VISIBLE)
            //user 입력란에 공백이 있는지에 대한 확인
            val fishspecies: String = mBinding.edFishspeciesWrite.getText().toString()
            val content: String = mBinding.edContentWrite.getText().toString()
            val password: String = mBinding.edPasswordWrite.getText().toString()
            if (fishspecies.isEmpty() && password.isEmpty()) {
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
        mBinding.imBackWrite.setOnClickListener { v -> finish() }
        mBinding.ibtGetPhotoWrite.setOnClickListener { v ->
            if (mBinding.imOneWrite.getDrawable() != null) {
                Toast.makeText(this@ActivityWritePost, "이미지는 최대 1장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                launchPhotoPicker()
            }
        }
        mBinding.imOneCancelWrite.setOnClickListener { v ->
            mBinding.imOneWrite.setImageResource(0)
            if (!imageUriList.isEmpty()) {
                Log.i("##INFO", "onViewClick(): delete Image to imageUriList")
                imageUriList.removeAt(0)
            }
            if (!bitmapList.isEmpty()) {
                Log.i("##INFO", "onViewClick(): delete Image to bitmapList")
                bitmapList.removeAt(0)
            }
        }
        if (!bitmapList.isEmpty()) {
            if (bitmapList.size == 2) {
                bitmapList.removeAt(1)
            } else {
                bitmapList.removeAt(0)
            }
        }
    }


    private fun getImageUri(imageBitmap: Bitmap) {
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.getReference()
        val randomNum = (Math.random() * 100000).toInt()
        val mountainsRef: StorageReference =
            storageRef.child(mBinding.edFishspeciesWrite.text.toString() + randomNum.toString() + ".jpg")
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
                    Log.i("##INFO", "onSuccess(): getImageUri = $uri")
                    if (imageUriList.size < 2) {
                        imageUriList.add(uri.toString())
                        Log.i("##INFO", "onSuccess(): bimLIst.size = " + bitmapList.size)
                    }
                    addPost()
                })
            })
    }

    private fun addPost() {
        val fishspecies: String = mBinding.edFishspeciesWrite.getText().toString()
        val content: String = mBinding.edContentWrite.getText().toString()
        val password: String = mBinding.edPasswordWrite.getText().toString()
        auth = FirebaseAuth.getInstance()
        val nowuid = auth?.currentUser?.uid
        var nowUserNick = ""
        var res = false
        db.collection("Users").document(nowuid.toString()).get().addOnSuccessListener {
            nowUserNick = it.get("nickname").toString()
            Log.d("test1234", "$nowUserNick")
            //Log.d("test1234", "${it.data?.get("nickname")}")
            res = PresenterPost.instance!!.setPost(
                PostDataModel(
                    postId,
                    nowUserNick,
                    fishspecies,
                    content,
                    password,
                    ArrayList(),
                    imageUriList,
                    0,
                    favoritesList
                ), postId
            )
            if (res) {
                Toast.makeText(this, "게시글 작성에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "게시글 작성에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
