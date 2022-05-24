package com.example.capstonefcls.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.capstonefcls.R;
import com.example.capstonefcls.datamodels.Member;



import java.util.ArrayList;

public class RankListViewAdapter extends BaseAdapter {

    ArrayList<Member> items;
    Context context;

    public RankListViewAdapter(ArrayList<Member> items, Context context){
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
        view = inflater.inflate(R.layout.rank_listview_item, viewGroup, false);

        TextView tv_nickname = view.findViewById(R.id.rank_nickname);
        tv_nickname.setText(items.get(i).getName());

        TextView tv_point = view.findViewById(R.id.rank_point);
        tv_point.setText(Integer.toString(items.get(i).getPts()));


        return view;
    }
}