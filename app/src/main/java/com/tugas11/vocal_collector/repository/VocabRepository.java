package com.tugas11.vocal_collector.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.tugas11.vocal_collector.databases.VocabDatabase;
import com.tugas11.vocal_collector.models.VocabEntity;

import java.util.List;

public class VocabRepository {
    private static VocabRepository instance;

    public static VocabRepository getInstance(){
        if (instance != null){
            instance = null;
        }

        instance = new VocabRepository();

        return instance;
    }

    public LiveData<List<VocabEntity>> getAllData(Context context){
        return VocabDatabase.getInstance(context).vocabDao().getAllVocab();
    }

    public void insertData(Context context, VocabEntity entity){
        VocabDatabase.getInstance(context).vocabDao().insertAllVocab(entity);
    }

    public void deleteData(Context context, VocabEntity entity){
        VocabDatabase.getInstance(context).vocabDao().deleteVocab(entity);
    }

    public void updateData(Context context, VocabEntity entity){
        VocabDatabase.getInstance(context).vocabDao().updateVocab(entity.getWord(), entity.getDefinition(), entity.getDate(), entity.getStatus(), entity.getId());
    }
}
