package com.tugas11.vocal_collector;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.tugas11.vocal_collector.adapter.VocabAdapter;
import com.tugas11.vocal_collector.models.VocabEntity;
import com.tugas11.vocal_collector.utils.MyDividerItemDecoration;
import com.tugas11.vocal_collector.utils.RecyclerTouchListener;
import com.tugas11.vocal_collector.viewmodels.VocabViewModel;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private TextView data_message;

    private VocabViewModel viewModel;

    private MaterialSearchView searchView;
    private VocabAdapter adapter;

    private TextInputLayout word_input, definition_input;

    private RecyclerView recyclerView;

    private Button btn, btn_cancel;
    private Dialog dialog, detailDialog, updateDialog;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search_view);
        data_message = findViewById(R.id.data_message);

        viewModel = new ViewModelProvider(this).get(VocabViewModel.class);
        viewModel.init(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));

        adapter = new VocabAdapter();
        recyclerView.setAdapter(adapter);

        viewModel.getAllData().observe(this, new Observer<List<VocabEntity>>() {
            @Override
            public void onChanged(List<VocabEntity> vocabEntities) {

                adapter.setWords(vocabEntities);

                if (adapter.getItemCount() > 0) {
                    data_message.setVisibility(View.INVISIBLE);
                } else {
                    data_message.setVisibility(View.VISIBLE);
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                showVocabDetail(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDialog();
            }
        });
    }

    private void showVocabDetail(int position) {
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.animate);
        detailDialog = new AlertDialog.Builder(MainActivity.this)
                .setView(LayoutInflater.from(MainActivity.this).inflate(R.layout.show_vocab, null))
                .create();

        detailDialog.show();
        LinearLayout detail_line;
        TextInputEditText word_detail, detail;

        word_detail = detailDialog.findViewById(R.id.detail_word);
        detail = detailDialog.findViewById(R.id.detail);
        detail_line = detailDialog.findViewById(R.id.detal_line);


        word_detail.setText(adapter.getWords(position).getWord());
        detail.setText(adapter.getWords(position).getDefinition());

        word_detail.setKeyListener(null);
        detail.setKeyListener(null);

        Button detail_close = detailDialog.findViewById(R.id.detail_close);
        detail_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailDialog.dismiss();
            }
        });

        detail_line.startAnimation(animation);
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    updateDialog(position);
                } else if (which == 1) {
                    viewModel.deleteData(MainActivity.this, adapter.getWords(position));
                    adapter.notifyItemRemoved(position);
                }
            }
        });
        builder.show();
    }

    private void updateDialog(final int position) {
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.animate);
        LinearLayout updateLayout;
        final Button cancelUpdate, btnUpdate;
        final TextInputLayout word_update, definition_update;
        updateDialog = new AlertDialog.Builder(MainActivity.this)
                .setView(LayoutInflater.from(MainActivity.this).inflate(R.layout.update, null))
                .create();

        updateDialog.show();

        cancelUpdate = updateDialog.findViewById(R.id.cancel_update);
        btnUpdate = updateDialog.findViewById(R.id.btn_update);
        updateLayout = updateDialog.findViewById(R.id.update_dialog);
        word_update = updateDialog.findViewById(R.id.word_update);
        definition_update = updateDialog.findViewById(R.id.definition_update);

        cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (word_update.getEditText().getText().length() == 0 || definition_update.getEditText().getText().length() == 0){
                    Toast.makeText(MainActivity.this, "Word atau Definition tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else{
                    VocabEntity entity = new VocabEntity(word_update.getEditText().getText().toString(),definition_update.getEditText().getText().toString(),"aktif", new Date().getTime());
                    entity.setId(adapter.getWords(position).getId());
                    viewModel.updateData(MainActivity.this, entity);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                    updateDialog.dismiss();
                }
            }
        });
        updateLayout.startAnimation(animation);
    }

    private void insertDialog() {
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.animate);
        dialog = new AlertDialog.Builder(MainActivity.this)
                .setView(LayoutInflater.from(MainActivity.this).inflate(R.layout.insert_dialog, null))
                .create();

        dialog.show();

        word_input = dialog.findViewById(R.id.word_input);
        definition_input = dialog.findViewById(R.id.definition_input);
        layout = dialog.findViewById(R.id.dialogLinear);
        btn = dialog.findViewById(R.id.btn);
        btn_cancel = dialog.findViewById(R.id.cancel);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _word = word_input.getEditText().getText().toString();
                String _definition = definition_input.getEditText().getText().toString();
                if (_word.length() == 0 || _definition.length() == 0) {
                    Toast.makeText(MainActivity.this, "Word atau Definition tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.insertData(MainActivity.this, new VocabEntity(_word, _definition, "aktif", new Date().getTime()));
                    Toast.makeText(MainActivity.this, "Data telah diinput", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layout.startAnimation(animation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
