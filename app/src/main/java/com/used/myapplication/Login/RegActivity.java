package com.used.myapplication.Login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.used.myapplication.Model.Users;
import com.used.myapplication.R;

import java.util.Objects;

public class RegActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailId, passwd, lastnamereg, name_reg;
    private Button img_b, img_b2, interest1, interest2, interest3;
    private TextView tv;
    private final String USER = "Users";
    private DatabaseReference myRef;
    private RadioGroup radioGroup, radioGroup_country, radioGroup_interest;
    private RadioButton radioButton, radioButton_country, radioButton_interest;
    private Button choose, choose_country, choose_interest;
    boolean b = false, check = false, check2 = false;
    private String inter1 = "", inter2 = "", inter3 = "";
    private Boolean bInter1 = false, bInter2 = false, bInter3 = false;
    private String country, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        init();
    }

    /**
     * Инициализация данных
     */
    private void init() {
        mAuth = FirebaseAuth.getInstance();
        lastnamereg = findViewById(R.id.lastnamereg);
        name_reg = findViewById(R.id.namereg);
        emailId = findViewById(R.id.login);
        passwd = findViewById(R.id.passwd);
        myRef = FirebaseDatabase.getInstance().getReference(USER);
        tv = findViewById(R.id.action_text);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        img_b = findViewById(R.id.img_b);
        img_b2 = findViewById(R.id.img_b2);
        interest1 = findViewById(R.id.btnInterest1);
        interest2 = findViewById(R.id.btnInterest2);
        interest3 = findViewById(R.id.btnInterest3);
        img_b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCountry();
            }
        });
        img_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b) {
                    showDialogCity();
                } else {
                    Toast.makeText(RegActivity.this, " Выберите страну!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        interest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogInterest1();
            }
        });
        interest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogInterest2();
            }
        });
        interest3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogInterest3();
            }
        });
    }

    /**
     * Показать список стран
     */
    private void showDialogCountry() {
        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.popup_menu_country, null);

        radioGroup_country = view.findViewById(R.id.radioGroup_country);
        choose_country = view.findViewById(R.id.button2_country);

        alert.setView(view);
        alert.setCancelable(true);
        final AlertDialog dialog = alert.create();
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        choose_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton_country = view.findViewById(radioGroup_country.getCheckedRadioButtonId());
                if (radioButton_country == null) {
                    dialog.dismiss();
                } else {
                    img_b2.setText("  Ваша страна: " + radioButton_country.getText());
                    b = true;
                    check = true;
                    country = radioButton_country.getText().toString();
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * Показать список гародов
     */
    private void showDialogCity() {
        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.popup_menu_city2, null);

        radioGroup = view.findViewById(R.id.radioGroup);
        choose = view.findViewById(R.id.button2);

        alert.setView(view);
        alert.setCancelable(true);
        final AlertDialog dialog = alert.create();
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = view.findViewById(radioGroup.getCheckedRadioButtonId());
                if (radioButton == null) {
                    dialog.dismiss();
                } else {
                    img_b.setText("  Ваш город: " + radioButton.getText());
                    check2 = true;
                    city = radioButton.getText().toString();
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * Регистрация и проверка данных
     * @param v
     */
    public void onSignUp(View v) {
        final String email = emailId.getText().toString();
        final String last_name = lastnamereg.getText().toString();
        final String name = name_reg.getText().toString();
        String pwd = passwd.getText().toString();
        if (last_name.isEmpty()) {
            lastnamereg.setError("Поле не может быть пустым!");
            lastnamereg.requestFocus();
        } else if (name.isEmpty()) {
            name_reg.setError("Поле не может быть пустым!");
            name_reg.requestFocus();
        } else if (email.isEmpty()) {
            emailId.setError("Поле не может быть пустым!");
            emailId.requestFocus();
        } else if (pwd.isEmpty()) {
            passwd.setError("Поле не может быть пустым!");
            passwd.requestFocus();
        } else if (!check) {
            Toast.makeText(RegActivity.this, "Пожалуйста! Выберите страну!", Toast.LENGTH_SHORT).show();
        } else if (!check2) {
            Toast.makeText(RegActivity.this, "Пожалуйста! Выберите город!", Toast.LENGTH_SHORT).show();
        } else if (!bInter1 || !bInter2 || !bInter3) {
            Toast.makeText(RegActivity.this, "Пожалуйста! Выберите ваши интересы!", Toast.LENGTH_SHORT).show();
        } else if (inter1.equals(inter2) || inter2.equals(inter3) || inter1.equals(inter3)) {
            Toast.makeText(RegActivity.this, "Пожалуйста! Выберите разные интересы!", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Users users = new Users(last_name + " " + name, email, country, city, inter1, inter2, inter3);
                        myRef.push().setValue(users);
                        sendEmail();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * Верификация почты
     */
    private void sendEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Подвердите почту", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clickReg(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Список трех интересов (Custom alert dialog)
     */
    private void showDialogInterest1() {
        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.popup_menu_interest, null);

        radioGroup_interest = view.findViewById(R.id.radioInterest);
        choose_interest = view.findViewById(R.id.button_interest);

        alert.setView(view);
        alert.setCancelable(true);
        final AlertDialog dialog = alert.create();
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        choose_interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton_interest = view.findViewById(radioGroup_interest.getCheckedRadioButtonId());
                if (radioButton_interest == null) {
                    dialog.dismiss();
                } else {
                    interest1.setText("  " + radioButton_interest.getText().toString());
                    bInter1 = true;
                    inter1 = radioButton_interest.getText().toString();
                    dialog.dismiss();
                }
            }
        });
    }

    private void showDialogInterest2() {
        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.popup_menu_interest, null);

        radioGroup_interest = view.findViewById(R.id.radioInterest);
        choose_interest = view.findViewById(R.id.button_interest);

        alert.setView(view);
        alert.setCancelable(true);
        final AlertDialog dialog = alert.create();
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        choose_interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton_interest = view.findViewById(radioGroup_interest.getCheckedRadioButtonId());
                if (radioButton_interest == null) {
                    dialog.dismiss();
                } else {
                    interest2.setText("  " + radioButton_interest.getText().toString());
                    bInter2 = true;
                    inter2 = radioButton_interest.getText().toString();
                    dialog.dismiss();
                }
            }
        });
    }

    private void showDialogInterest3() {
        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.popup_menu_interest, null);

        radioGroup_interest = view.findViewById(R.id.radioInterest);
        choose_interest = view.findViewById(R.id.button_interest);

        alert.setView(view);
        alert.setCancelable(true);
        final AlertDialog dialog = alert.create();
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        choose_interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton_interest = view.findViewById(radioGroup_interest.getCheckedRadioButtonId());
                if (radioButton_interest == null) {
                    dialog.dismiss();
                } else {
                    interest3.setText("  " + radioButton_interest.getText().toString());
                    bInter3 = true;
                    inter3 = radioButton_interest.getText().toString();
                    dialog.dismiss();
                }
            }
        });
    }
}