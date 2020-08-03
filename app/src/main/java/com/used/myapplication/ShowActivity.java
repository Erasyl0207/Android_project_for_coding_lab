package com.used.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.used.myapplication.Adapters.AdapterShow;
import com.used.myapplication.Model.Cours;

import java.util.ArrayList;

public class ShowActivity extends AppCompatActivity {
    private DatabaseReference reference, reference_likes;
    private TextView textView, textView_site, textView_data, textView_email, textView_type, desc, textView_format;
    private ImageView imageView;
    private ArrayList<Cours> cours;
    private AdapterShow adapterShow;
    private RecyclerView recyclerView;
    private String NAME = "courses";
    private String LIKES = "Likes";
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_2);
        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterShow);
        cours = new ArrayList<Cours>();
        textView = findViewById(R.id.textView_title);
        textView_site = findViewById(R.id.textView_site);
        textView_email = findViewById(R.id.textView_email);
        textView_data = findViewById(R.id.textView_data);
        textView_type = findViewById(R.id.textView_type);
        textView_format = findViewById(R.id.textView_format);
        desc = findViewById(R.id.editText);
        imageView = findViewById(R.id.title_image);
        getIntentMain();
        reference = FirebaseDatabase.getInstance().getReference().child(NAME);
        reference_likes = FirebaseDatabase.getInstance().getReference().child(LIKES);

        adapterShow = new AdapterShow(cours);
        recyclerView.setAdapter(adapterShow);

        getDB();
    }

    /**
     * Получает курсы из базы данных
     */
    public void getDB() {
        reference = FirebaseDatabase.getInstance().getReference().child(NAME);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (cours.size() > 0) cours.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.child("name").getValue().equals(text)) {
                        Cours c = dataSnapshot1.getValue(Cours.class);
                        cours.add(c);
                    }
                }
                adapterShow = new AdapterShow(cours);
                recyclerView.setAdapter(adapterShow);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addValueEventListener(eventListener);
    }

    /**
     * Получает Intent от MainActivity(HomeFragment , ProfileFragment , ChangeableFragment)
     */
    private void getIntentMain() {
        Intent intent = getIntent();
        if (intent != null) {
            text = intent.getStringExtra("key");
            textView.setText(intent.getStringExtra("key"));
            textView_data.setText(intent.getStringExtra("data"));
            desc.setText(intent.getStringExtra("desc"));
            textView_email.setText(intent.getStringExtra("email"));
            textView_site.setText(intent.getStringExtra("site"));
            textView_type.setText(intent.getStringExtra("type"));
            textView_format.setText(intent.getStringExtra("form"));
            Picasso.get().load(intent.getStringExtra("picture")).into(imageView);
        }
    }

}
