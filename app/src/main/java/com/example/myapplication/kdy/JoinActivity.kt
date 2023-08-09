package com.example.myapplication.kdy

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.databinding.ActivityJoinBinding


class JoinActivity : AppCompatActivity() {
    val TAG: String = "JoinActivity"
    var isExistBlank = false
    var isPWSame = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityJoinBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //회원가입 버튼
        binding.joinbutton.setOnClickListener {

            val name: String = binding.nameInput.text.toString()
            val nickname: String = binding.nicknameInput.text.toString()
            val tel: String = binding.telInput.text.toString()
            val id: String = binding.idInput.text.toString()
            val pw: String = binding.pwInput.text.toString()
            val pw2: String = binding.pw2Input.text.toString()

            // 사용자가 필수 입력 사항을 모두 입력하지 않은 경우
            if (id.isEmpty() || name.isEmpty() || nickname.isEmpty() || tel.isEmpty() || pw.isEmpty() || pw2.isEmpty()) {
                isExistBlank = true
            } else {
                if (pw == pw2) {
                    isPWSame = true
                }
            }

            if (!isExistBlank && isPWSame) {

                // 회원가입 성공 토스트 메세지 띄우기
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                // 유저가 입력한 id, pw를 쉐어드에 저장한다.
                val sharedPreference = getSharedPreferences("user name", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("id", id)
                editor.putString("pw", pw)
                editor.apply()
                // 로그인 화면으로 이동
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            } else {
                // 상태에 따라 다른 다이얼로그 띄워주기
                if (isExistBlank) {   // 작성 안한 항목이 있을 경우
                    dialog("blank")
                    isExistBlank = false
                } else if (!isPWSame) { // 입력한 비밀번호가 다를 경우
                    dialog("not same")
                    isPWSame = false
                }
            }

        }
        binding.backlogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    // 회원가입 실패시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        // 빈 칸 있을 경우
        if(type == "blank"){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        }
        // 비밀번호 다를 경우
        else if(type == "not same"){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 틀렸습니다")
        }

        val dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }

}