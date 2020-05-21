package com.tugas11.vocal_collector.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tugas11.vocal_collector.R;
import com.tugas11.vocal_collector.models.VocabEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VocabAdapter extends RecyclerView.Adapter<VocabAdapter.VocabViewHolder> implements Filterable {
    private List<VocabEntity> al;
    private List<VocabEntity> list_filter;

    public static class VocabViewHolder extends RecyclerView.ViewHolder{
        private TextView time, word, status;
        public VocabViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            word = itemView.findViewById(R.id.word);
            status = itemView.findViewById(R.id.status);
        }
    }

    public void setWords(List<VocabEntity> al){
        this.al = al;
        list_filter = new ArrayList<>(al);
        notifyDataSetChanged();
    }

    public VocabEntity getWords(int position){
        return al.get(position);
    }

    @NonNull
    @Override
    public VocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        VocabViewHolder viewHolder = new VocabViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VocabViewHolder holder, int position) {
        holder.time.setText(new SimpleDateFormat("EE. dd/MM/yyyy, hh:mm:ss a").format(al.get(position).getDate()));
        holder.word.setText(al.get(position).getWord());
        holder.status.setText(Html.fromHtml("&#8226;"));
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<VocabEntity> filterList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0)
            {
                filterList.addAll(list_filter);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (VocabEntity vocab : list_filter) {
                    if (vocab.getWord().toLowerCase().contains(filterPattern)){
                        filterList.add(vocab);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            al.clear();
            al.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
