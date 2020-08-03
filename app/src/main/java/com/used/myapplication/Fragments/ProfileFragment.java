package com.used.myapplication.Fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.used.myapplication.Adapters.SaveAdapter;
import com.used.myapplication.Model.Cours;
import com.used.myapplication.Model.Users;
import com.used.myapplication.Login.LoginActivity;
import com.used.myapplication.R;
import com.used.myapplication.ShowActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileFragment extends Fragment implements SaveAdapter.MyOnItemClick1 {
    private DatabaseReference reference_users, reference_saves, reference;
    private FirebaseUser firebaseUser;
    private String COURS = "courses";
    private String USER = "Users";
    private String SAVES = "Saves";
    private RecyclerView recyclerView_saves;
    private Users u;
    private String email_Db;
    private String name_Db, location_Db;
    private ArrayList<Users> user;
    private SaveAdapter myAdapter;
    private ArrayList<Cours> list_saves;
    private List<String> mySaves;
    private TextView name, email, location;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView_saves = view.findViewById(R.id.recyclerView_saves);
        recyclerView_saves.setLayoutManager(new LinearLayoutManager(getContext()));
        list_saves = new ArrayList<>();
        myAdapter = new SaveAdapter(list_saves);
        recyclerView_saves.setAdapter(myAdapter);
        user = new ArrayList<>();
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        location = view.findViewById(R.id.location);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        profileView();
        mySaves();
    }

    /**
     * Информация о пользователе
     */
    private void profileView() {
        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        final String user_id = intent.getStringExtra("user_id");
        reference_users = FirebaseDatabase.getInstance().getReference().child(USER);
        reference_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.child("email").getValue().equals(user_id)) {
                        u = dataSnapshot1.getValue(Users.class);
                        assert u != null;
                        email_Db = u.getEmail();
                        name_Db = u.getName();
                        location_Db = u.getCountry() + ", " + u.getCity();
                        email.setText(email_Db);
                        name.setText(name_Db);
                        location.setText(location_Db);
                        user.add(u);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Сохраненные курсы пользователя
     */
    private void mySaves() {
        mySaves = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference_saves = FirebaseDatabase.getInstance().getReference(SAVES)
                .child(firebaseUser.getUid());
        reference_saves.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mySaves.size() > 0) mySaves.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    mySaves.add(Snapshot.getKey());
                }
                readSaves();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readSaves() {
        reference = FirebaseDatabase.getInstance().getReference().child(COURS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (list_saves.size() > 0) list_saves.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cours cours1 = snapshot.getValue(Cours.class);
                    for (String id : mySaves) {
                        if (cours1.getName().equals(id)) {
                            list_saves.add(cours1);
                        }
                    }
                }
                myAdapter.setOnItemClick(ProfileFragment.this);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param position позиция курса
     *                 Передает Intent в ShowActivity
     */
    @Override
    public void OnItemClick(int position) {
        Cours cours = list_saves.get(position);
        Intent intent = new Intent(getContext(), ShowActivity.class);
        intent.putExtra("picture", cours.getPicture());
        intent.putExtra("key", cours.getName());
        intent.putExtra("type", cours.getType());
        intent.putExtra("desc", cours.getDescription());
        intent.putExtra("data", cours.getData());
        intent.putExtra("email", cours.getEmail());
        intent.putExtra("site", cours.getSite());
        intent.putExtra("form", cours.getFormat());
        startActivity(intent);
    }

    /**
     * Выход из аккаунта
     */
    public void openDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Выйти")
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("Да", null)
                .setNegativeButton("Отмена", null)
                .show();
        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}
