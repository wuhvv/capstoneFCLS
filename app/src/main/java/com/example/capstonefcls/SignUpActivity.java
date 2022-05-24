package com.example.capstonefcls;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstonefcls.datamodels.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Init firestore
        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.signUpButton:
                    signUp();
                    break;
            }
        }
    };

    private void signUp(){

        String email = ((EditText) findViewById(R.id.loginEmail)).getText().toString();
        String nickname = ((EditText) findViewById(R.id.SignUpNickname)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextTextPassword)).getText().toString();
        String passwordCheck = ((EditText) findViewById(R.id.editTextTextPassword2)).getText().toString();

        Boolean submitFlag = true;

        if(email.isEmpty()){
            Toast.makeText(SignUpActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }

        if(nickname.isEmpty()){
            Toast.makeText(SignUpActivity.this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }

        if(password.isEmpty()){
            Toast.makeText(SignUpActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }
        if(passwordCheck.isEmpty()){
            Toast.makeText(SignUpActivity.this, "확인 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }
        if(password.length() < 6){
            Toast.makeText(SignUpActivity.this, "비밀번호를 6자리 이상 입력해주세요", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }
        if(!password.equals(passwordCheck)){
            Toast.makeText(SignUpActivity.this, "비밀번호가 서로 다릅니다.", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }

        if(submitFlag){

            progressDialog.setMessage("등록 중입니다. 기다려 주세요...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // 성공 할 경우
                                Toast.makeText(SignUpActivity.this, "회원가입이 성공했습니다", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();

                                Member m = new Member(nickname,1, 500);
                                db.collection("members").document(user.getUid()).set(m);

                                // 회원가입시 메인 액티비티로 이동
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                // 실패할 경우
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }


    }

}