package com.example.capstonefcls.mainflagments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.capstonefcls.R;
import com.example.capstonefcls.map.CoorData;
import com.example.capstonefcls.map.CoordParser;
import com.example.capstonefcls.map.PolylineSeter;
import com.example.capstonefcls.map.SearchCoord;
import com.example.capstonefcls.map.SearchParser;

import net.daum.mf.map.api.MapView;

import java.util.ArrayList;


public class MapFragment extends Fragment {

    public MapFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        v.findViewById(R.id.boardFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_mapFragment_to_boardFragment);
            }
        });

        v.findViewById(R.id.rankFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_mapFragment_to_rankFragment);
            }
        });

        v.findViewById(R.id.profileFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_mapFragment_to_profileFragment);
            }
        });


        /////////////////////////////////

        MapView mapView = new MapView(getActivity());


        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view); //뷰그룹 사용
        mapViewContainer.addView(mapView);

        //트래킹 모드
        //mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        //Searchparser(테스트)
        SearchParser searchParser = new SearchParser();


        //Coordparse (테스트)
        CoordParser apiData = new CoordParser();


        //PolyLineSeter클래스
        PolylineSeter polylineSeter = new PolylineSeter();

        //텍스트뷰 (테스트)
        TextView textView1 = (TextView) v.findViewById(R.id.text1) ;

        //검색(SearchView) 테스트


        SearchView searchView = v.findViewById(R.id.SearchView1);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ArrayList<SearchCoord> scData = searchParser.getCoord(s);
                ArrayList<CoorData> dataArr = apiData.getData(scData.get(0).SearchHelper());
                polylineSeter.set_poly(mapView, dataArr);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        /////////////////////////////////


        return v;
    }
}