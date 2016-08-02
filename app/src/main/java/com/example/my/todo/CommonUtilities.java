package com.example.my.todo;

/**
 * Created by naveen on 6/26/2016.
 */
public class CommonUtilities {

    private String id;
    private String task;
    private String adate;
    private String photoUrl;
    private String name;
    private String atime;

    public CommonUtilities() {
    }

    public CommonUtilities(String task,  String name, String adate, String atime ) {
        this.task = task;
        this.adate = adate;
        this.photoUrl = photoUrl;
        this.name = name;
        this.atime = atime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAdate() {
        return adate;
    }

    public void setAdate(String adate) {
        this.adate = adate;
    }


    public String getAtime() {
        return atime;
    }

    public void setAtime(String atime) {
        this.atime = atime;
    }





}
