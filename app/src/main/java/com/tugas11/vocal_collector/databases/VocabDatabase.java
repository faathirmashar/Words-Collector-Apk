package com.tugas11.vocal_collector.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tugas11.vocal_collector.dao.VocabDao;
import com.tugas11.vocal_collector.models.VocabEntity;

@Database(entities = {VocabEntity.class}, version = 1, exportSchema = false)
public abstract class VocabDatabase extends RoomDatabase {
    public abstract VocabDao vocabDao();
    private static VocabDatabase instance;

    public static VocabDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), VocabDatabase.class, "Vocab_collector").allowMainThreadQueries().build();
        }

        return instance;
    }
}
