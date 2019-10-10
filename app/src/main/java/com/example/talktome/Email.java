package com.example.talktome;

public class Email {
    protected Email(){};

    protected Email(String receiver, String subject, String content, String sender){
        this.receiver = receiver;
        this.subject = subject;
        this.content = content;
        this.sender = sender;
    }

    private String receiver;
    private String subject;
    private String content;
    private String sender;

    public String getContent() {
        return content;
    }
    public String getReceiver(){
        return receiver;
    }
    public String getSubject(){
        return subject;
    }
    public String getSender(){
        return sender;
    }
    public void setReceiver(String r){
        this.receiver = r;
    }
    public void setSubject(String s){
        this.subject = s;
    }
    public void setContent(String c){
        this.content = c;
    }
    public void setSender(String se){
        this.sender = se;
    }
}
