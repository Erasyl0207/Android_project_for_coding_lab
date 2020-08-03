package com.used.myapplication.Fragments;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.used.myapplication.Adapters.MyAdapter;
import com.used.myapplication.Model.Cours;
import com.used.myapplication.R;
import com.used.myapplication.ShowActivity;

import java.util.ArrayList;

public class ChangeableFragment extends Fragment implements MyAdapter.MyOnItemClick {
    private DatabaseReference reference;
    private String COURS = "courses";
    private Cours cours;
    private ArrayList<Cours> list;
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private String myString;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        assert bundle != null;
        myString = bundle.getString("put_all");
        return inflater.inflate(R.layout.fragment_changeable, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.changable_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<Cours>();
        adapter = new MyAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClick(ChangeableFragment.this);

        getDataFromDB(myString);
    }

    /**
     * Достает из базы данных соответствующий тип курса по городу или интересам
     * @param str
     */
    private void getDataFromDB(final String str) {
        reference = FirebaseDatabase.getInstance().getReference().child(COURS);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (list.size() > 0) list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    cours = ds.getValue(Cours.class);
                    assert cours != null;
                    check(str);
                }
                adapter = new MyAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClick(ChangeableFragment.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        reference.addValueEventListener(eventListener);
    }

    /**
     * Проверка
     * @param str
     */
    private void check(String str) {
        if (cours.getCountry().equals(str)) {
            list.add(cours);
        }
        if (cours.getCity().equals(str)) {
            list.add(cours);
        }
        if (cours.getType().equals(str)) {
            list.add(cours);
        }
    }

    /**
     * @param position позиция курса
     *                 Передает Intent в ShowActivity
     */
    @Override
    public void OnItemClick(int position) {
        Cours cours = list.get(position);
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

}
