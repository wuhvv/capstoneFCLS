package com.example.capstonefcls.map;

import android.graphics.Color;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class PolylineSeter {
    public void set_poly(MapView mapView, ArrayList<CoorData> dataArr) {
        // Polyline 좌표 지정.
        mapView.removeAllPolylines();
        for(int i=0; i<dataArr.size(); i++) { // 매 CoorData마다 폴리라인 객체 생성
            MapPolyline polyline = new MapPolyline();
            polyline.setLineColor(Color.argb(128, i*10, 51, 0));
            polyline.setTag(1000);
            for(int x=0; x<dataArr.get(i).Coords.length/2; x++) {
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(dataArr.get(i).Coords[x*2+1]), Double.parseDouble(dataArr.get(i).Coords[x*2])));
            }
            mapView.addPolyline(polyline); // 폴리라인 객체 지도에 올리기
        }

        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
        mapView.fitMapViewAreaToShowAllPolylines();
    }
}