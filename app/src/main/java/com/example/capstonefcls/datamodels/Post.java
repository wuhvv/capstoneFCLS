package com.example.capstonefcls.datamodels;

public class Post {

    private String author; // 작성자
    private String title; // 제목
    private String content; // 내용
    private String contentPicURL; // 내용 사진
    private String createdAt; // 작성일

    public Post() {}

    public Post(String author, String title, String content, String createdAt) {
        this.author = author;
        this.content = content;
        this.title = title;
        this.createdAt = createdAt;
    }

    public String getTitle(){
        return this.title;
    }

    public String getAuthor(){return this.author;}

    public String getCreatedAt(){return this.createdAt;}


    public String getContent(){return this.content;}

}