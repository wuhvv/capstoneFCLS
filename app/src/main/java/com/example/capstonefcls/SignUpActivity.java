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
            Toast.makeText(SignUpActivity.this, "???????????? ??????????????????", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }

        if(nickname.isEmpty()){
            Toast.makeText(SignUpActivity.this, "???????????? ??????????????????", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }

        if(password.isEmpty()){
            Toast.makeText(SignUpActivity.this, "??????????????? ??????????????????", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }
        if(passwordCheck.isEmpty()){
            Toast.makeText(SignUpActivity.this, "?????? ??????????????? ??????????????????", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }
        if(password.length() < 6){
            Toast.makeText(SignUpActivity.this, "??????????????? 6?????? ?????? ??????????????????", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }
        if(!password.equals(passwordCheck)){
            Toast.makeText(SignUpActivity.this, "??????????????? ?????? ????????????.", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }

        if(submitFlag){

            progressDialog.setMessage("?????? ????????????. ????????? ?????????...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // ?????? ??? ??????
                                Toast.makeText(SignUpActivity.this, "??????????????? ??????????????????", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();

                                Member m = new Member(user.getUid(), nickname,1, 500);
                                db.collection("members").document(user.getUid()).set(m);

                                // ??????????????? ?????? ??????????????? ??????
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                // ????????? ??????
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }


    }

}