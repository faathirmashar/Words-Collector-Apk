package com.tugas11.vocal_collector.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.tugas11.vocal_collector.models.VocabEntity;

import java.util.List;

@Dao
public interface VocabDao {
    @Query("SELECT * FROM vocab")
    LiveData<List<VocabEntity>> getAllVocab();

    @Insert
    void insertAllVocab(VocabEntity... vocabEntities);

    @Delete
    void deleteVocab(VocabEntity vocabEntity);

    @Query("UPDATE vocab SET word=:word, definition=:definition, date=:date, status=:status WHERE id=:id")
    void updateVocab(String word, String definition, long date, String status, int id);
}
