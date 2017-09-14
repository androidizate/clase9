package com.androidizate.clase9.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * @author Andres Oller
 */
@Entity
public class Todo {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "note")
    private String note;
    @ColumnInfo(name = "status")
    private int status;
    @ColumnInfo(name = "createdAt")
    private Date createdAt;

    public Todo() {
    }

    public Todo(String note, int status) {
        this.note = note;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
