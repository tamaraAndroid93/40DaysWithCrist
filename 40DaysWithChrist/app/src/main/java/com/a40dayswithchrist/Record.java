package com.a40dayswithchrist;

/**
 * Created by Alen on 8/11/2016.
 */
public class Record {

    private long id;
    private String path;



    public long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Record( String path) {

        this.path = path;

    }
    public Record(){

    }
}



