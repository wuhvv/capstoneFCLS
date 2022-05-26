package com.example.capstonefcls;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstonefcls.datamodels.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class BoardWriteActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    boolean imageUploadFlag = false;

    private static final String TAG = "BoardWriteActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);



        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Init firestore
        db = FirebaseFirestore.getInstance();
        // init firestrogaeeeeeee
        storage = FirebaseStorage.getInstance();


        findViewById(R.id.board_write_submit).setOnClickListener(onClickListener);
        findViewById(R.id.board_write_image).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.board_write_image:
                    imagePic();
                    break;
                case R.id.board_write_submit:
                    postSubmit();
                    break;
            }
        }
    };

    private void postSubmit() {

        String title = ((EditText) findViewById(R.id.board_write_title)).getText().toString();
        String content = ((EditText) findViewById(R.id.board_write_content)).getText().toString();

        Boolean submitFlag = true;

        if (title.isEmpty()) {
            Toast.makeText(BoardWriteActivity.this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }
        if (content.isEmpty()) {
            Toast.makeText(BoardWriteActivity.this, "내용를 입력해주세요", Toast.LENGTH_SHORT).show();
            submitFlag = false;
        }

        if (submitFlag) {


            FirebaseUser user = mAuth.getCurrentUser();

            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA);
            String current = format.format(currentTime);


            Post p = new Post(user.getUid(), "관리자", title, content, current);

            DocumentReference PostRef = db.collection("posts").document();
            PostRef.set(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(BoardWriteActivity.this, "글쓰기 성공", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BoardWriteActivity.this, "글쓰기 실패", Toast.LENGTH_SHORT).show();
                        }
                    });

            if(imageUploadFlag){
                imageUpload(PostRef.getId());
            }

            finish();
        }

    }

    private void imagePic(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);

    }

    private void imageUpload(String id){

        StorageReference storageRef = storage.getReference();
        StorageReference PostImageRef = storageRef.child("Post_images/"+id+".jpg");

        ImageView imageView = ((ImageView) findViewById(R.id.board_write_image));

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = PostImageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(BoardWriteActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 100){
            ((ImageView) findViewById(R.id.board_write_image)).setImageURI(data.getData());
            imageUploadFlag = true;
        }
    }
}