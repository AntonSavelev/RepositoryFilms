package com.example.repositoryfilms.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.repositoryfilms.R;
import com.example.repositoryfilms.model.Character;

import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    Listener listener;
    private List<Character> characters;

    public MyAdapter() {
        this.characters = Collections.EMPTY_LIST;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
        final Character character = characters.get(position);
        final String characterName = character.getName();
        holder.bind(character);

        CardView cardView = holder.cardView;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(characterName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public void setData(List<Character> characters) {
        this.characters = characters;
        notifyDataSetChanged();
    }

    public interface Listener {
        void onClick(String characterName);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCharacterName;
        TextView tvCharacterHeight;
        TextView tvCharacterMass;
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
            tvCharacterName = cardView.findViewById(R.id.tvCharacterName);
            tvCharacterHeight = cardView.findViewById(R.id.tvCharacterHeight);
            tvCharacterMass = cardView.findViewById(R.id.tvCharacterMass);
        }

        public void bind(Character character) {
            tvCharacterName.setText("Name: " + character.getName());
            tvCharacterHeight.setText("Height: " + character.getHeight());
            tvCharacterMass.setText("Mass: " + character.getMass());
        }
    }
}
