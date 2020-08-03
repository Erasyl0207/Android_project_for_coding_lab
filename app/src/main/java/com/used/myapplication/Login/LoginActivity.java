package com.used.myapplication.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.used.myapplication.MainActivity;
import com.used.myapplication.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailId, passwd;
    private Button signIn;
    private TextView tv;
    private FirebaseAuth mauth;
    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    /**
     * Инициализация данных
     */
    private void init() {
        mAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.login);
        passwd = findViewById(R.id.passwd);
        mauth = FirebaseAuth.getInstance();
        signIn = findViewById(R.id.btnSign);
        tv = findViewById(R.id.action_text);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * @param v Вход в аккаунт
     */
    public void onSignIn(View v) {
        final String email = emailId.getText().toString();
        String pwd = passwd.getText().toString();
        if (email.isEmpty()) {
            emailId.setError("Поле не может быть пустым!");
            emailId.requestFocus();
        } else if (pwd.isEmpty()) {
            passwd.setError("Поле не может быть пустым!");
            passwd.requestFocus();
        } else {
            mauth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mauth.getCurrentUser();
                        assert user != null;
                        if (user.isEmailVerified()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("user_id", email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Подвердите почту", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * Проверка авторизации пользователя
     */
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.isEmailVerified()) {
                String str = currentUser.getEmail();
                Toast.makeText(this, "Вы вошли как " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user_id", str);
                startActivity(intent);
            }
        }
    }

    public void clickLog(View v) {
        Intent intent = new Intent(this, RegActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Override метода on back pressed для activity
     */
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(a);
                }
            }, 1000);
        }
    }
}