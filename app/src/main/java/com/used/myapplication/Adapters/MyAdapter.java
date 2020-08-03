package com.used.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;
import com.used.myapplication.CommentsActivity;
import com.used.myapplication.Model.Cours;
import com.used.myapplication.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<Cours> cours;
    private String LIKES = "Likes";
    private String SAVED = "Saves";
    Context context;
    private FirebaseUser firebaseUser;
    private MyOnItemClick onItemClick;

    public interface MyOnItemClick {
        void OnItemClick(int position);
    }

    public void setOnItemClick(MyOnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public MyAdapter(Context context, ArrayList<Cours> p) {
        this.context = context;
        cours = p;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.MyViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Cours cours_item = cours.get(position);
        holder.title.setText(cours.get(position).getName());
        holder.type.setText(cours.get(position).getType());
        holder.data.setText(cours.get(position).getData());
        holder.site.setText(cours.get(position).getSite());
        Picasso.get().load(cours.get(position).getPicture()).into(holder.picture);

        isLikes(cours_item.getName(), holder.disLike);
        countLikes(holder.likeCounter, cours_item.getName());
        isSaved(cours_item.getName(), holder.notFavorite);
        getComments(cours_item.getName(), holder.comments);
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

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("postId", cours_item.getName());
                intent.putExtra("publisherId", cours_item.getName());
                context.startActivity(intent);
            }
        });
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("postId", cours_item.getName());
                intent.putExtra("publisherId", cours_item.getName());
                context.startActivity(intent);
            }
        });
    }

    /**
     * Получает из базы данных комменты
     * @param postId
     * @param textView
     */
    private void getComments(String postId, final TextView textView) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textView.setText("View all " + dataSnapshot.getChildrenCount() + " comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param postID    Пост на которую поставили лайк
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
     * @param likes  Возвращает getChildrenCount (количество лайков)
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, data, type, site;
        ImageView picture;
        ImageButton disLike, notFavorite;
        ImageView comment;
        TextView likeCounter, comments;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_view);
            data = itemView.findViewById(R.id.data_view);
            type = itemView.findViewById(R.id.type_view);
            site = itemView.findViewById(R.id.site_view);
            picture = itemView.findViewById(R.id.picture);
            comment = itemView.findViewById(R.id.image_comment);
            disLike = itemView.findViewById(R.id.disLike);
            likeCounter = itemView.findViewById(R.id.textView_like);
            notFavorite = itemView.findViewById(R.id.notFavorite);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClick.OnItemClick(position);
                        }
                    }
                }
            });
            comments = itemView.findViewById(R.id.comments);
        }
    }
}
