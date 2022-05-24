package com.example.capstonefcls.map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchParser {
    public ArrayList<SearchCoord> getCoord(String mName) {
        //return data 부분
        ArrayList<SearchCoord> searchCoords = new ArrayList<SearchCoord>();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    //요청 Url
                    String searchUrl = "https://api.vworld.kr/req/search?service=search&request=search&version=2.0&crs=EPSG:4326&size=10&page=1&type=place&format=xml&errorformat=xml&key=F931BD24-945F-3AA9-8CB7-853B5D40C5A8&query=" + mName;
                    URL url = new URL(searchUrl);
                    InputStream is = url.openStream();

                    //xmlParser 생성
                    XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = xmlFactory.newPullParser();
                    parser.setInput(is,"utf-8");

                    //xml과 관련된 변수들
                    boolean isTitle = false;
                    boolean isThat = false;
                    boolean isFirst = true;
                    boolean isCate = false;
                    boolean isMount = false;
                    boolean isX = false;
                    boolean isY = false;
                    String X = "";
                    String Y = "";
                    String Title = "";
                    String Cate = "";

                    // 파싱 시작
                    while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                        int type = parser.getEventType();
                        SearchCoord sc = new SearchCoord();

                        //태그 검사(태그가 gml:posList인 경우 찾기)
                        if(type == XmlPullParser.START_TAG) {
                            if (parser.getName().equals("category")) {
                                isCate = true;
                            }
                            else if(parser.getName().equals("title")){
                                isTitle = true;
                            }
                            else if(parser.getName().equals("x")){
                                isX = true;
                            }
                            else if(parser.getName().equals("y")){
                                isY = true;
                            }
                        }

                        else if(type == XmlPullParser.TEXT) { //텍스트확인
                            if(isTitle){ //태그가 타이틀일 때
                                if(isFirst){
                                    Title = parser.getText();
                                }
                                if(parser.getText()==Title){
                                    isThat = true;
                                }
                                isTitle = false;
                            }
                            else if(isCate) { //태그가 카테고리일때
                                if(isFirst){
                                    Cate = parser.getText();
                                    isFirst = false;
                                }
                                if(parser.getText()==Cate){//데이터 분류가 산이라면
                                    isMount = true; //이 데이터가 산임을 명시
                                }
                                isCate = false; //카테고리 태그 끝
                            }
                            else if(isX) { //태그가 x일 때
                                if(isThat && isMount){ //데이터가 입력한 산이라면
                                    X = parser.getText(); //x좌표 X에 저장
                                }
                                isX = false; //x 태그 끝
                            }
                            else if(isY) { //태그가 y일 때
                                if(isThat && isMount) { //데이터가 입력한 산이라면
                                    Y = parser.getText(); // y좌표 Y에 저장
                                }
                                isY = false; //y 태그 끝
                            }
                        }

                        // y태그가 끝날 때데이터 추가 ()
                        else if(type == XmlPullParser.END_TAG && parser.getName().equals("y") && isThat && isMount) {
                            sc.x = X;
                            sc.y = Y;
                            searchCoords.add(sc);
                            isThat = false; // 산 데이터에 대한 x,y 좌표 저장 끝났으므로
                            isMount = false; //isThat과 isMount 초기화
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
        return searchCoords;
    }
}