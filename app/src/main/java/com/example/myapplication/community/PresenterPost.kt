package com.example.myapplication.community

import android.util.Log
import com.example.myapplication.databinding.ActivityMainBoardBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

/**
 * 게시글 작성, 수정, 삭제, 조회를 위한 Presenter
 * fireabase에 접근하여 데이터를 가져오거나 수정할 수 있다.
 */
class PresenterPost private constructor() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding : ActivityMainBoardBinding

    fun setPost(postInfo: PostDataModel?, postId: String): Boolean {
        Log.d("test1234", "------$postInfo---------")
        if (postInfo == null) return false
        if (postInfo.fishspecies.isEmpty() || postInfo.content.isEmpty() || postInfo.password.isEmpty()) return false

        if (!postId.isEmpty()) {
            //case -> 게시글 수정
            db.collection(COLLECTION_PATH).document(postId).update("Posts", postInfo)
                .addOnSuccessListener { }
                .addOnFailureListener { e: Exception ->
                    Log.e(
                        TAG,
                        "setPost: error >>$e"
                    )
                }
        } else {
            //case -> 게시글 첫 작성
            val posts: MutableMap<String, PostDataModel> = HashMap<String, PostDataModel>()
            posts["Posts"] = postInfo
            db.collection(COLLECTION_PATH).add(posts)
                .addOnSuccessListener { documentReference: DocumentReference? ->
                    Log.i("##INFO", "success = ${documentReference?.id}(): ");
                }
                .addOnFailureListener { e: Exception ->
                    Log.e(
                        TAG,
                        "setPost: error >>$e"
                    )
                }
        }
        return true
    }

    fun getPost(callback: IPostsResultCallback): Boolean {
        //firestore에서 COLLECTION_PATH와 일치하는 데이터를 가져온다
        var count = 0
        db.collection(COLLECTION_PATH).get().addOnSuccessListener { queryDocumentSnapshots ->
            val postsList: ArrayList<PostDataModel> = ArrayList<PostDataModel>()
            if (!queryDocumentSnapshots.isEmpty) {
                for (snapshot in queryDocumentSnapshots) {
                    //getId() = documentId를 가져온다
                    var res: HashMap<String?, PostDataModel?>? =
                        HashMap<String?, PostDataModel?>()
                    res = snapshot["Posts"] as HashMap<String?, PostDataModel?>?
                    if (res != null) {
                        val data = PostDataModel()
                        data.id = snapshot.id
                        data.nickname = java.lang.String.valueOf(res["nickname"])
                        data.fishspecies = java.lang.String.valueOf(res["fishspecies"])
                        data.content = java.lang.String.valueOf(res["content"])
                        data.password = java.lang.String.valueOf(res["password"])
                        data.replies = java.util.ArrayList<Replies>(res["replies"] as Collection<Replies?>?)
                        data.pictures = ArrayList(res["pictures"] as Collection<String>?)
                        data.favorites = res.get("favorites") as HashMap<String, Boolean>
                        data.favoriteCount = java.lang.String.valueOf(res.get("favoriteCount")).toInt()
                        data.wherecatchfish = java.lang.String.valueOf(res.get("wherecatchfish"))
                        postsList.add(data)
                        count++
                        callback.onResult(postsList)
                    }
                }
            }
        }
        return true
    }

    fun setReply(postInfo: PostDataModel): Boolean {
        postInfo.id.let {
            db.collection(COLLECTION_PATH).document(it).update("Posts", postInfo)
                .addOnSuccessListener {
                    Log.i(
                        "##INFO",
                        "setReply(): success"
                    )

                }.addOnFailureListener { e -> Log.i("##INFO", "setReply(): e = " + e.message) }
        }
        return true
    }

    fun deleteReply(postInfo: PostDataModel): Boolean {
        postInfo.id?.let {
            db.collection(COLLECTION_PATH).document(it).update("Posts", postInfo)
                .addOnSuccessListener {
                    Log.i(
                        "##INFO",
                        "deleteReply(): success"
                    )
                }.addOnFailureListener { e ->
                    Log.i(
                        "##INFO",
                        "deleteReply(): e = " + e.message
                    )
                }
        }
        return true
    }

//    //게시글을 삭제하는 메서드
//    fun deletePost(postInfo: PostDataModel): Boolean {
//        val updates = hashMapOf<String, Any>(
//            "Posts" to FieldValue.delete(),
//        )
//
//        postInfo.id.let {
//            db.collection(COLLECTION_PATH).document(it).delete().addOnSuccessListener {
//                Log.i(
//                    "##INFO",
//                    "deletePost(): success"
//                )
//            }.addOnFailureListener { e ->
//                Log.i(
//                    "##INFO",
//                    "deletePost(): e = " + e.message
//                )
//            }
//        }
//        return true
//    }

    interface IPostsResultCallback {
        fun onResult(list: ArrayList<PostDataModel>?)
        fun onError(erMsg: String?)
    }

    companion object {
        private const val TAG = "##H"
        private const val COLLECTION_PATH = "BoardPosts"
        private var INSTANCE: PresenterPost? = null


        val instance: PresenterPost?
            get() {
                if (INSTANCE == null) {
                    INSTANCE = PresenterPost()
                }
                return INSTANCE
            }
    }
}