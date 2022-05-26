package com.example.capstonefcls;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


public class BoardReadActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;

    private static final String TAG = "BoardReadActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_read);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Init firestore
        db = FirebaseFirestore.getInstance();

        user = mAuth.getCurrentUser();

        String id = getIntent().getStringExtra("id");

        // Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        findViewById(R.id.post_set_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mDV = LayoutInflater.from(BoardReadActivity.this).inflate(R.layout.post_set_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardReadActivity.this);
                builder.setView(mDV).setTitle("수정/삭제");
                AlertDialog dialogClick = builder.show();

                dialogClick.findViewById(R.id.post_set_edit_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "edit", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), BoardEditActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        finish();
                    }
                });

                dialogClick.findViewById(R.id.post_set_del_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });



        DocumentReference docRef = db.collection("posts").document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Post p = document.toObject(Post.class);
                        String uid = user.getUid().toString();

                        if(uid != null){
                            if(p.getAuthor_uid().equals(uid)){
                                ((Button) findViewById(R.id.post_set_btn)).setVisibility(View.VISIBLE);
                            }else{
                            }
                        }


                        ((TextView) findViewById(R.id.board_read_title)).setText(p.getTitle());
                        ((TextView) findViewById(R.id.board_read_author)).setText(p.getAuthor());
                        ((TextView) findViewById(R.id.board_read_content)).setText(p.getContent());
                        ((TextView) findViewById(R.id.board_read_time)).setText(p.getCreatedAt());

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        ImageView riv = ((ImageView)findViewById(R.id.board_read_image));

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