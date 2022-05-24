package com.example.capstonefcls.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.capstonefcls.R;
import com.example.capstonefcls.datamodels.Post;

import java.util.ArrayList;

public class BoardListViewAdapter extends BaseAdapter {

    ArrayList<Post> items;
    Context context;

    public BoardListViewAdapter(ArrayList<Post> items, Context context){
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.board_listview_item, viewGroup, false);

        TextView tv_title = view.findViewById(R.id.textview_title);
        tv_title.setText(items.get(i).getTitle());

        TextView tv_content = view.findViewById(R.id.textview_content);
        tv_content.setText(items.get(i).getContent());

        TextView tv_time = view.findViewById(R.id.textview_time);
        tv_time.setText(items.get(i).getCreatedAt());

        return view;
    }
}
