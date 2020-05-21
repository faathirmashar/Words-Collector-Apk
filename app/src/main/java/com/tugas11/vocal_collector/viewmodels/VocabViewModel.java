package com.tugas11.vocal_collector.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tugas11.vocal_collector.databases.VocabDatabase;
import com.tugas11.vocal_collector.models.VocabEntity;
import com.tugas11.vocal_collector.repository.VocabRepository;

import java.util.List;

public class VocabViewModel extends ViewModel {
    private LiveData<List<VocabEntity>> data;

    public void init(Context context){
        if (data != null) {
            data = null;
        }

        data = VocabRepository.getInstance().getAllData(context);
    }

    public LiveData<List<VocabEntity>> getAllData(){
        return data;
    }

    public void insertData(Context context, VocabEntity vocabEntity){
        VocabRepository.getInstance().insertData(context, vocabEntity);
    }

    public void deleteData(Context context, VocabEntity vocabEntity){
        VocabRepository.getInstance().deleteData(context, vocabEntity);
    }

    public void updateData(Context context, VocabEntity vocabEntity){
        VocabRepository.getInstance().updateData(context, vocabEntity);
    }
}
