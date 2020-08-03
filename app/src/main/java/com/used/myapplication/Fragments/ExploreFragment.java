package com.used.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.used.myapplication.Model.Users;
import com.used.myapplication.R;

import java.util.Objects;

public class ExploreFragment extends Fragment implements View.OnClickListener {
    private DatabaseReference reference_users;
    private String USER = "Users";
    private Users u;
    private ImageView imageView_country, imageView_city, img_interest1, img_interest2, img_interest3;
    private TextView textView_country, textView_city, tv_inter1, tv_inter2, tv_inter3;
    private String put_country, put_city, put_interest_1, put_interest_2, put_interest_3;
    private ImageView img11,img12,img13,img14,img15,img16,img17,img18,img19;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView_country = view.findViewById(R.id.image_country);
        imageView_city = view.findViewById(R.id.image_city);
        img_interest1 = view.findViewById(R.id.img_1);
        img_interest2 = view.findViewById(R.id.img_2);
        img_interest3 = view.findViewById(R.id.img_3);
        textView_city = view.findViewById(R.id.tv_city);
        textView_country = view.findViewById(R.id.tv_country);

        tv_inter1 = view.findViewById(R.id.inter1);
        tv_inter2 = view.findViewById(R.id.inter2);
        tv_inter3 = view.findViewById(R.id.inter3);
        getInterest();

        imageView_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Fragment fragment = new ChangeableFragment();
                bundle.putString("put_all", put_country);
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();
            }
        });
        imageView_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Fragment fragment = new ChangeableFragment();
                bundle.putString("put_all", put_city);
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();
            }
        });
        img_interest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Fragment fragment = new ChangeableFragment();
                bundle.putString("put_all", put_interest_1);
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();

            }
        });
        img_interest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Fragment fragment = new ChangeableFragment();
                bundle.putString("put_all", put_interest_2);
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();

            }
        });
        img_interest3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Fragment fragment = new ChangeableFragment();
                bundle.putString("put_all", put_interest_3);
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();

            }
        });
        initImages();
    }

    /**
     * Получает из базы данных список интересов пользователя
     */
    private void getInterest() {
        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        final String user_id = intent.getStringExtra("user_id");
        reference_users = FirebaseDatabase.getInstance().getReference().child(USER);
        reference_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (Objects.equals(dataSnapshot1.child("email").getValue(), user_id)) {
                        u = dataSnapshot1.getValue(Users.class);
                        assert u != null;
                        put_country = u.getCountry();
                        put_city = u.getCity();

                        put_interest_1 = u.getInterest1();
                        put_interest_2 = u.getInterest2();
                        put_interest_3 = u.getInterest3();

                        textView_city.setText(put_city);
                        textView_country.setText(put_country);
                        tv_inter1.setText(put_interest_1);
                        tv_inter2.setText(put_interest_2);
                        tv_inter3.setText(put_interest_3);
                        setImage(put_interest_1);
                        setImage2(put_interest_2);
                        setImage3(put_interest_3);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * Менят рисунки курсов на соответствующий
     * @param tv_img1
     */
    private void setImage(String tv_img1) {
        switch (tv_img1) {
            case "Программирование":
                img_interest1.setImageResource(R.drawable.programming);
                break;
            case "Бизнес":
                img_interest1.setImageResource(R.drawable.business);
                break;
            case "Психология":
                img_interest1.setImageResource(R.drawable.psychology);
                break;
            case "Английский язык":
                img_interest1.setImageResource(R.drawable.english);
                break;
            case "Фотография":
                img_interest1.setImageResource(R.drawable.photography);
                break;
            case "Саморазвитие":
                img_interest1.setImageResource(R.drawable.self_development);
                break;
            case "Видеосъемка":
                img_interest1.setImageResource(R.drawable.videography);
                break;
            case "Дизайн":
                img_interest1.setImageResource(R.drawable.design);
                break;
            case "Финансы":
                img_interest1.setImageResource(R.drawable.finance);
                break;
            default:
                break;
        }

    }

    private void setImage2(String tv_img1) {
        switch (tv_img1) {
            case "Программирование":
                img_interest2.setImageResource(R.drawable.programming);
                break;
            case "Бизнес":
                img_interest2.setImageResource(R.drawable.business);
                break;
            case "Психология":
                img_interest2.setImageResource(R.drawable.psychology);
                break;
            case "Английский язык":
                img_interest2.setImageResource(R.drawable.english);
                break;
            case "Фотография":
                img_interest2.setImageResource(R.drawable.photography);
                break;
            case "Саморазвитие":
                img_interest2.setImageResource(R.drawable.self_development);
                break;
            case "Видеосъемка":
                img_interest2.setImageResource(R.drawable.videography);
                break;
            case "Дизайн":
                img_interest2.setImageResource(R.drawable.design);
                break;
            case "Финансы":
                img_interest2.setImageResource(R.drawable.finance);
                break;
            default:
                break;
        }
    }

    private void setImage3(String tv_img1) {
        switch (tv_img1) {
            case "Программирование":
                img_interest3.setImageResource(R.drawable.programming);
                break;
            case "Бизнес":
                img_interest3.setImageResource(R.drawable.business);
                break;
            case "Психология":
                img_interest3.setImageResource(R.drawable.psychology);
                break;
            case "Английский язык":
                img_interest3.setImageResource(R.drawable.english);
                break;
            case "Фотография":
                img_interest3.setImageResource(R.drawable.photography);
                break;
            case "Саморазвитие":
                img_interest3.setImageResource(R.drawable.self_development);
                break;
            case "Видеосъемка":
                img_interest3.setImageResource(R.drawable.videography);
                break;
            case "Дизайн":
                img_interest3.setImageResource(R.drawable.design);
                break;
            case "Финансы":
                img_interest3.setImageResource(R.drawable.finance);
                break;
            default:
                break;
        }

    }

    private void initImages(){
        img11 = Objects.requireNonNull(getView()).findViewById(R.id.img_11);
        img11.setOnClickListener(this);
        img12 = getView().findViewById(R.id.img_12);
        img12.setOnClickListener(this);
        img13 = getView().findViewById(R.id.img_13);
        img13.setOnClickListener(this);
        img14 = getView().findViewById(R.id.img_14);
        img14.setOnClickListener(this);
        img15 = getView().findViewById(R.id.img_15);
        img15.setOnClickListener(this);
        img16 = getView().findViewById(R.id.img_16);
        img16.setOnClickListener(this);
        img17 = getView().findViewById(R.id.img_17);
        img17.setOnClickListener(this);
        img18 = getView().findViewById(R.id.img_18);
        img18.setOnClickListener(this);
        img19 = getView().findViewById(R.id.img_19);
        img19.setOnClickListener(this);
    }

    /**
     * Передача типа курса в ChangeableActivity
     * @param v
     */
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        Fragment fragment = new ChangeableFragment();
        switch (v.getId()) {
            case R.id.img_11:
                bundle.putString("put_all", "Английский язык");
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();
                break;
            case R.id.img_12:
                bundle.putString("put_all", "Бизнес");
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();
                break;
            case R.id.img_13:
                bundle.putString("put_all", "Видеосъемка");
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();
                break;
            case R.id.img_14:
                bundle.putString("put_all", "Дизайн");
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();
                break;
            case R.id.img_15:
                bundle.putString("put_all", "Программирование");
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();
                break;
            case R.id.img_16:
                bundle.putString("put_all", "Психология");
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();
                break;
            case R.id.img_17:
                bundle.putString("put_all", "Саморазвитие");
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();
                break;
            case R.id.img_18:
                bundle.putString("put_all", "Финансы");
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();
                break;
            case R.id.img_19:
                bundle.putString("put_all", "Фотография");
                fragment.setArguments(bundle);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "ChangeableFragment").addToBackStack("ChangeableFragment").commit();
                break;
        }
    }
}
