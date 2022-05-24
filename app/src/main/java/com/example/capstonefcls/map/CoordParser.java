package com.example.capstonefcls.map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CoordParser {
    public ArrayList<CoorData> getData(String boxData) {
        //return data 부분
        ArrayList<CoorData> dataArr = new ArrayList<CoorData>();
        Thread t = new Thread() {
            @Override
            public  void run() {
                try {
                    //요청 Url
                    String fullurl = "https://api.vworld.kr/req/data?service=data&version=2.0&request=getfeature&key=F931BD24-945F-3AA9-8CB7-853B5D40C5A8&domain=http://localhost:8080&format=xml&data=LT_L_FRSTCLIMB&crs=epsg:4326&geomfilter=BOX("+boxData;
                    URL url = new URL(fullurl);
                    InputStream is = url.openStream();

                    //xmlParser 생성
                    XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = xmlFactory.newPullParser();
                    parser.setInput(is,"utf-8");

                    //xml과 관련된 변수들
                    boolean isCoords = false;
                    String Coords = "";

                    // 파싱 시작
                    while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                        int type = parser.getEventType();
                        CoorData data = new CoorData();

                        //태그 검사(태그가 gml:posList인 경우 찾기)
                        if(type == XmlPullParser.START_TAG) {
                            if (parser.getName().equals("gml:posList")) {
                                isCoords = true;
                            }
                        }
                        //텍스트 확인 (Coords에 텍스트 임시 저장)
                        else if(type == XmlPullParser.TEXT) {
                            if(isCoords) {
                                Coords = parser.getText();
                                isCoords = false;
                            }
                        }
                        // 데이터 추가 (Coords데이터 공백으로 스플릿하여 저장)
                        else if(type == XmlPullParser.END_TAG && parser.getName().equals("gml:posList")) {
                            data.Coords = Coords.split(" ");

                            dataArr.add(data);
                        }

                        type = parser.next();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return dataArr;
    }
}