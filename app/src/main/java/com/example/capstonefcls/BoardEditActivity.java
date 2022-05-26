package com.example.capstonefcls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.capstonefcls.datamodels.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class BoardEditActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    String id;
    String createdAt;


    private static final String TAG = "BoardEditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_edit);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Init firestore
        db = FirebaseFirestore.getInstance();

        id = getIntent().getStringExtra("id");

        findViewById(R.id.board_edit_submit).setOnClickListener(onClickListener);

        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        readPostData(id);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.board_edit_submit:
                    postEditSubmit();
                    break;
            }
        }
    };

    private void postEditSubmit() {

        String title = ((EditText) findViewById(R.id.board_edit_title)).getText().toString();
        String content = ((EditText) findViewById(R.id.board_edit_content)).getText().toString();
        String EditedcreatedAt = createdAt;

        Boolean submitFlag = true;

        if (title.isEmpty()) {
            Toast.makeText(BoardEditActivity.this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }
        if (content.isEmpty()) {
            Toast.makeText(BoardEditActivity.this, "내용를 입력해주세요", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }

        if (submitFlag) {


            FirebaseUser user = mAuth.getCurrentUser();

            EditedcreatedAt += "(수정됨)";

            Post p = new Post(user.getUid(),"관리자", title, content, EditedcreatedAt);

            DocumentReference PostRef = db.collection("posts").document(id);
            PostRef.set(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(BoardEditActivity.this, "글쓰기 성공", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BoardEditActivity.this, "글쓰기 실패", Toast.LENGTH_SHORT).show();
                        }
                    });

            finish();
        }

    }

    private void readPostData(String id){

        DocumentReference docRef = db.collection("posts").document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Post p = document.toObject(Post.class);

                        ((EditText) findViewById(R.id.board_edit_title)).setText(p.getTitle());
                        ((EditText) findViewById(R.id.board_edit_content)).setText(p.getContent());
                        createdAt = p.getCreatedAt();

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        ImageView riv = ((ImageView)findViewById(R.id.board_edit_image));

                        storageReference.child("Post_images/"+id+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getApplicationContext())
                                        .load(uri)
                                        .into(riv);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }



}