package com.example.task91p;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {
    Context context;
    ArrayList<String>itemname = new ArrayList<String>();
    private RecyclerViewClickListener listener;

    public Myadapter(Context context, ArrayList<String> item, RecyclerViewClickListener listener) {
        this.context = context;
        this.itemname = item;
        this.listener = listener;


    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item, null);
        MyViewHolder myViewHoder = new MyViewHolder(view);
        return myViewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String names = itemname.get(position);

        holder.name.setText(names);




    }

    @Override
    public int getItemCount() {
        return itemname.size();
    }


    public interface RecyclerViewClickListener{
        void  onClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.itemname);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());
        }
    }
}
