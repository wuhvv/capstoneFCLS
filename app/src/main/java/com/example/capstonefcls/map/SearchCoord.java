package com.example.capstonefcls.map;

public class SearchCoord {
    String x;
    String y;
    public String SearchHelper() {
        Double minX = Double.parseDouble(x)-0.01;
        Double maxX = Double.parseDouble(x)+0.01;
        Double minY = Double.parseDouble(y)-0.01;
        Double maxY = Double.parseDouble(y)+0.01;
        String result = minX.toString()+","+minY.toString()+","+maxX.toString()+","+maxY.toString()+")";
        return result;
    }
}