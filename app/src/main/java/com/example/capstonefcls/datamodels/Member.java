package com.example.capstonefcls.datamodels;

import java.util.ArrayList;

public class Member {

    private String uid;
    private String name; // 사용자 이름
    private String profileURL; // 프로필 사진
    private int level; // 일반 사용자 or 관리자 등..
    private int pts; // 포인트
    private ArrayList<String> achievements; // 업적 관련 map
    // private achieve 클래스를 만들지

    public Member() {}


    public Member(String uid, String name, String profileURL, int level, int pts) {
        this.uid = uid;
        this.name = name;
        this.profileURL = profileURL;
        this.level = level;
        this.pts = pts;
        this.achievements = new ArrayList<>();
//        achievements.add("업적 값");
//        achievements.add("업적 값2");
    }

    public Member(String uid, String name, int level, int pts) {
        this.uid = uid;
        this.name = name;
        this.level = level;
        this.pts = pts;
    }

    public Member(String name){
        this.name = name;
    }

    public String getUid(){return this.uid;}

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileURL(){return this.profileURL; }

    public int getLevel(){return this.level;}
    public int getPts(){return this.pts;}
    public ArrayList<String> getAchievements(){return this.achievements;}
}