package com.example.flo.ui.signin

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.remote.AuthService
import com.example.flo.data.remote.Result
import com.example.flo.ui.signup.SignUpActivity
import com.example.flo.data.entities.User
import com.example.flo.databinding.ActivityLoginBinding
import com.example.flo.ui.main.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class LoginActivity: AppCompatActivity(), LoginView {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 회원가입 버튼 클릭 리스너 설정
        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // 로그인 버튼 클릭 리스너 설정
        binding.loginSignInBtn.setOnClickListener {
            login()
        }

        // 카카오 로그인 버튼 클릭 리스너 설정
        binding.loginKakakoLoginIv.setOnClickListener {
            loginWithKakao()
        }
        binding.loginCloseIv.setOnClickListener {
            closeLoginAndOpenMain()
        }
    }

    private fun loginWithKakao() {
        // 카카오톡 설치 여부 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                handleLoginResult(token, error)
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                handleLoginResult(token, error)
            }
        }
    }

    private fun handleLoginResult(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            Log.e("KakaoLogin", "로그인 실패", error)
            Toast.makeText(this, "카카오 로그인 실패: ${error.message}", Toast.LENGTH_SHORT).show()
        } else if (token != null) {
            Log.i("KakaoLogin", "로그인 성공 ${token.accessToken}")
            // 카카오 로그인 성공 시 JWT 값을 저장하고 다음 화면으로 이동
            saveJwt(token.accessToken)
            startMainActivity()
        }
    }

    private fun saveJwt(jwt: String) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putString("jwt", jwt)
        editor.apply()
    }

    private fun login() {
        // 입력값 검증
        if (binding.loginIdEt.text.toString()
                .isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.loginPasswordEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 이메일 및 비밀번호 가져오기
        val email: String =
            binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val pwd: String = binding.loginPasswordEt.text.toString()

        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(User(email, pwd, ""))
    }

    private fun closeLoginAndOpenMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()  // 현재 액티비티 종료
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun saveJwt2(jwt: String) {
        val spf = getSharedPreferences("auth2", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }

    override fun onLoginSuccess(code: Int, result: Result) {
        when (code) {
            1000 -> {
                saveJwt2(result.jwt)
                startMainActivity()
            }
        }
    }

    override fun onLoginFailure() {
        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
    }

    // 카카오계정으로 로그인 공통 callback 구성
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
            Toast.makeText(this, "로그인 실패티비", Toast.LENGTH_SHORT).show()
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            Toast.makeText(this, "로그인 성공티비", Toast.LENGTH_SHORT).show()
        }
    }
}
