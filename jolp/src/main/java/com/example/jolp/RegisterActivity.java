package com.example.jolp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    public DatabaseReference databaseReference;
    public FirebaseDatabase firebaseDatabase;
    protected FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText et_id, et_pass, et_name, et_school;
    Button btn_register;
    TextView txt_login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 아이디, 비번, 학교, 이름 값 찾아주기
        et_id = (EditText) findViewById(R.id.edit_id);
        et_pass = (EditText) findViewById(R.id.edit_pw);
        et_school = (EditText) findViewById(R.id.edit_sc);
        et_name = (EditText) findViewById(R.id.edit_name);
        txt_login = (TextView) findViewById(R.id.text_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String userID = et_id.getText().toString().trim();
                String userPass = et_pass.getText().toString().trim();
                String userSchool = et_school.getText().toString().trim();
                String userName = et_name.getText().toString().trim();

                firebaseDatabase = FirebaseDatabase.getInstance("https://jolp-a5446-default-rtdb.asia-southeast1.firebasedatabase.app/");
                databaseReference = firebaseDatabase.getReference("UserData");
                //회원정보를 제대로 입력했을 경우
                if (validateEmail(userID) && validatePass(userPass) && validateSc(userSchool) && validateNm(userName)) {
                    mAuth.createUserWithEmailAndPassword(userID, userPass)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        UserData data = new UserData(userID, userPass, userSchool, userName);
                                        databaseReference.child(userSchool).child(mAuth.getCurrentUser().getUid()).setValue(data).
                                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        //회원가입 성공시
                                                        if(task.isSuccessful()) {
                                                            Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                               //             Log.d("dbTAG", "회원가입 완료");
                                                        }
                                                    }
                                                });
                                    } else {
                                        // 계정이 중복된 경우
                                        Toast.makeText(RegisterActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } /* else {
                    //회원정보가 하나라도 채워지지 않았을 경우
                    Toast.makeText(RegisterActivity.this, "회원정보를 입력해 주세요", Toast.LENGTH_LONG).show();
                } */
            }
        });
    }

    private boolean validateEmail(String s) {
        if (s.isEmpty() || s.equals("")) {
            Toast.makeText(RegisterActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            Toast.makeText(RegisterActivity.this, "이메일을 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    private boolean validatePass(String s){
        if(s.isEmpty() || s.equals("")){
            Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }
    private boolean validateSc(String s){
        if(s.isEmpty() || s.equals("")){
            Toast.makeText(RegisterActivity.this, "학교를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    private boolean validateNm(String s){
        if(s.isEmpty() || s.equals("")){
            Toast.makeText(RegisterActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

/*    protected void createUser(String email, String password, String school, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserData data = new UserData(email, password, school, name);
                            FirebaseDatabase.getInstance().getReference("UserData")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(data).
                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //회원가입 성공시
                                            Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(RegisterActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    } */
@Override
protected void onStart() {
    super.onStart();
    //if the user already logged in then it will automatically send on Dashboard/MainActivity
    if (mAuth.getCurrentUser() != null) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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
