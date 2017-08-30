package com.example.repositoryfilms;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private List<People> results;
    Drawable drawable;
    Context context;

    public MyAdapter(Context context, List<People> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.image);
        drawable = ContextCompat.getDrawable(context, R.drawable.species_1080);
        imageView.setImageDrawable(drawable);
        TextView textView = cardView.findViewById(R.id.info_text);
        People people = results.get(position);
        textView.setText(people.getName());
    }

    @Override
    public int getItemCount() {
        if (results == null)
            return 0;
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }
}
