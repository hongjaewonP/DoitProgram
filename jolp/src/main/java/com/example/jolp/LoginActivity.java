package com.example.jolp;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import android.view.View.OnClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText et_id, et_pass;
    private Button btn_login1, btn_register1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        et_id = (EditText) findViewById(R.id.edit_id);
        et_pass = (EditText) findViewById(R.id.edit_pw);
        btn_login1 = (Button) findViewById(R.id.btn_login1);

        btn_login1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // EditText에 현재 입력된 값을 가져온다.
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();

                if (validateReq(userID) && validateReq(userPass)) {
                    mAuth.signInWithEmailAndPassword(userID, userPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // 로그인 성공
                                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // 로그인 실패
                                        Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //firebase에 사용자 계정이 존재할 경우 메인화면으로 이동
  /*      firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"존재하지 않는 사용자입니다.",Toast.LENGTH_SHORT).show();
                }
            }
        }; */

        btn_register1 = (Button) findViewById(R.id.btn_register1);
        // 회원가입 버튼을 클릭 시 수행
        btn_register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent2);
                finish();
            }
        });

    }

    private boolean validateReq(String r){
        if(r.isEmpty() || r.equals("")){
            Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            return true;
        }
    }

    //이전에 로그아웃 안했으면 가장 최근에 로그인한 아이디로 접속됨
    @Override
    protected void onStart() {
        super.onStart();
        //if the user already logged in then it will automatically send on Dashboard/MainActivity
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        //   mAuth.addAuthStateListener(firebaseAuthListener); >>강제종료 시켜서 지움
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

}
