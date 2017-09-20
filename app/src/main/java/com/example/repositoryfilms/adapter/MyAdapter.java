package com.example.repositoryfilms.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.repositoryfilms.R;
import com.example.repositoryfilms.model.Character;

import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Character> characters;

    public MyAdapter() {
        this.characters = Collections.EMPTY_LIST;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        Character character = characters.get(position);
        holder.bind(character);
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public void setData(List<Character> characters) {
        this.characters = characters;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCharacterName;
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
            tvCharacterName = cardView.findViewById(R.id.info_text);
        }

        public void bind(Character character) {
            tvCharacterName.setText(character.getName());
        }
    }
}
