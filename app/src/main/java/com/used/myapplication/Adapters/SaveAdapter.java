package com.used.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.used.myapplication.Model.Cours;
import com.used.myapplication.R;

import java.util.ArrayList;

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.ViewHolder> {
    private ArrayList<Cours> cours;
    private MyOnItemClick1 myOnItemClick1;
    public interface MyOnItemClick1{
        void OnItemClick(int position);
    }
    public void setOnItemClick(MyOnItemClick1 myOnItemClick1){
        this.myOnItemClick1 = myOnItemClick1;
    }
    public SaveAdapter(ArrayList<Cours> cours) {
        this.cours = cours;
    }

    @NonNull
    @Override
    public SaveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.save_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SaveAdapter.ViewHolder holder, int position) {
        holder.title.setText(cours.get(position).getName());
        holder.type.setText(cours.get(position).getType());
        Picasso.get().load(cours.get(position).getPicture()).into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return cours.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title , type;
        ImageView picture;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_view1);
            type = itemView.findViewById(R.id.type_view1);
            picture = itemView.findViewById(R.id.picture1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myOnItemClick1!= null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            myOnItemClick1.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
