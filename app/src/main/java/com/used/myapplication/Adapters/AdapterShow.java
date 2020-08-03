package com.used.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.used.myapplication.Model.Cours;
import com.used.myapplication.R;

import java.util.ArrayList;

public class AdapterShow extends RecyclerView.Adapter<AdapterShow.ViewHolder> {
    private ArrayList<Cours> cours;
    private String LIKES = "Likes";
    private String SAVED = "Saves";
    private FirebaseUser firebaseUser;

    public AdapterShow(ArrayList<Cours> cours) {
        this.cours = cours;
    }

    @NonNull
    @Override
    public AdapterShow.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.forlikeview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterShow.ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Cours cours_item = cours.get(position);

        isLikes(cours_item.getName(), holder.disLike);
        countLikes(holder.likeCounter, cours_item.getName());
        isSaved(cours_item.getName(), holder.notFavorite);
        holder.disLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.disLike.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child(LIKES).child(cours_item.getName())
                            .child(firebaseUser.getUid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child(LIKES).child(cours_item.getName())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });
        holder.notFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.notFavorite.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference().child(SAVED).child(firebaseUser.getUid())
                            .child(cours_item.getName()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child(SAVED).child(firebaseUser.getUid())
                            .child(cours_item.getName()).removeValue();
                }
            }
        });
    }

    /**
     *
     * @param postID Пост на которую поставили лайк
     * @param imageView кнопка лайка
     */
    private void isLikes(String postID, final ImageView imageView) {
        DatabaseReference reference_likes = FirebaseDatabase.getInstance().getReference().child(LIKES).child(postID);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference_likes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                assert firebaseUser != null;
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.like);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.dislike);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     *
     * @param likes Возвращает getChildrenCount (количество лайков)
     * @param postID
     */

    private void countLikes(final TextView likes, String postID) {
        DatabaseReference reference_likes = FirebaseDatabase.getInstance().getReference().child(LIKES).child(postID);
        reference_likes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount() + " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     *
     * @param postID
     * @param imageView
     */
    private void isSaved(final String postID, final ImageView imageView) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(SAVED)
                .child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postID).exists()) {
                    imageView.setImageResource(R.drawable.favorite);
                    imageView.setTag("saved");
                } else {
                    imageView.setImageResource(R.drawable.not_favorite);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return cours.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton disLike, notFavorite;
        TextView likeCounter;

        public ViewHolder(View itemView) {
            super(itemView);
            disLike = itemView.findViewById(R.id.like_button);
            likeCounter = itemView.findViewById(R.id.textView_count);
            notFavorite = itemView.findViewById(R.id.notFavorite);
        }
    }
}


