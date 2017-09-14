package com.androidizate.clase9.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author Andres Oller
 */
@Entity
public class Tag {

    @PrimaryKey
    private int id;
    @ColumnInfo(name = "tagName")
    private String tagName;

    public Tag() {

    }

    public Tag(int id, String tagName) {
        this.id = id;
        this.tagName = tagName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTagName(String tag_name) {
        this.tagName = tag_name;
    }

    public int getId() {
        return this.id;
    }

    public String getTagName() {
        return this.tagName;
    }
}
