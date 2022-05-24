package com.example.capstonefcls.mainflagments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.capstonefcls.BoardReadActivity;
import com.example.capstonefcls.R;
import com.example.capstonefcls.adapter.BoardListViewAdapter;
import com.example.capstonefcls.adapter.RankListViewAdapter;
import com.example.capstonefcls.datamodels.Member;
import com.example.capstonefcls.datamodels.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;


public class RankFragment extends Fragment {

    FirebaseFirestore db;

    ListView listview;
    ArrayList<Member> items;
    ArrayList<String> ids;
    RankListViewAdapter adapter;

    public RankFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_rank, container, false);

        v.findViewById(R.id.boardFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_rankFragment_to_boardFragment);
            }
        });

        v.findViewById(R.id.mapFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_rankFragment_to_mapFragment);
            }
        });

        v.findViewById(R.id.profileFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_rankFragment_to_profileFragment);
            }
        });



        db = FirebaseFirestore.getInstance();

        listview = v.findViewById(R.id.rank_listview);
        items = new ArrayList<>();
        ids = new ArrayList<>();

        db.collection("members").orderBy("pts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                Member m = document.toObject(Member.class);


                                // items.add(new Post(document.getData().get("첫번째 데이터").toString(),document.getData().get("두번째 데이터").toString(),"내용", "1"));
                                items.add(m);
                                ids.add(document.getId().toString());
                            }

                            Collections.reverse(items);
                            Collections.reverse(ids);
                            adapter = new RankListViewAdapter(items, getContext());

                            listview.setAdapter(adapter);


//                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
////                            Intent intent = new Intent(getApplicationContext(), BoardReadActivity.class);
////                            intent.putExtra("title", items.get(i).getTitle());
////                            intent.putExtra("author", items.get(i).getAuthor());
////                            intent.putExtra("content", items.get(i).getContent());
////                            intent.putExtra("time", items.get(i).getCreatedAt());
////                            startActivity(intent);
//
//
//                                    Intent intent = new Intent(getContext(), BoardReadActivity.class);
//                                    intent.putExtra("id", ids.get(i));
//                                    startActivity(intent);
//
//
//
//                                }
//                            });


                        } else {
                            // Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        return v;
    }
}