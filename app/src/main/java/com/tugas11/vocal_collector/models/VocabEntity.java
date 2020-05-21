package com.tugas11.vocal_collector.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vocab")
public class VocabEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String word;
    private String definition;
    private String status;
    private long date;

    public VocabEntity(String word, String definition, String status, long date) {
        this.word = word;
        this.definition = definition;
        this.status = status;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }
}
