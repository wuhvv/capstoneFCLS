package com.example.capstonefcls.mainflagments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.capstonefcls.BoardReadActivity;
import com.example.capstonefcls.BoardWriteActivity;
import com.example.capstonefcls.MainActivity;
import com.example.capstonefcls.R;
import com.example.capstonefcls.adapter.BoardListViewAdapter;
import com.example.capstonefcls.datamodels.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;


public class BoardFragment extends Fragment {

    FirebaseFirestore db;

    ListView listview;
    ArrayList<Post> items;
    ArrayList<String> ids;
    BoardListViewAdapter adapter;


    public BoardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_board, container, false);

        v.findViewById(R.id.mapFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_boardFragment_to_mapFragment);
            }
        });

        v.findViewById(R.id.rankFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_boardFragment_to_rankFragment);
            }
        });

        v.findViewById(R.id.profileFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_boardFragment_to_profileFragment);
            }
        });

        v.findViewById(R.id.board_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "글쓰기 버튼 눌림", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), BoardWriteActivity.class);
                startActivity(intent);
            }
        });



        db = FirebaseFirestore.getInstance();

        listview = v.findViewById(R.id.listview);
        items = new ArrayList<>();
        ids = new ArrayList<>();

        db.collection("posts").orderBy("createdAt")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                Post p = document.toObject(Post.class);


                                // items.add(new Post(document.getData().get("첫번째 데이터").toString(),document.getData().get("두번째 데이터").toString(),"내용", "1"));
                                items.add(p);
                                ids.add(document.getId().toString());
                            }

                            Collections.reverse(items);
                            Collections.reverse(ids);
                            adapter = new BoardListViewAdapter(items, getContext());

                            listview.setAdapter(adapter);


                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


//                            Intent intent = new Intent(getApplicationContext(), BoardReadActivity.class);
//                            intent.putExtra("title", items.get(i).getTitle());
//                            intent.putExtra("author", items.get(i).getAuthor());
//                            intent.putExtra("content", items.get(i).getContent());
//                            intent.putExtra("time", items.get(i).getCreatedAt());
//                            startActivity(intent);


                                    Intent intent = new Intent(getContext(), BoardReadActivity.class);
                                    intent.putExtra("id", ids.get(i));
                                    startActivity(intent);



                                }
                            });


                        } else {
                            // Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });




        return v;
    }
}