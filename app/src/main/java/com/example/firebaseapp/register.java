package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class register extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText editText_email, editText_username, editText_password, editText_confirmpassword;
    private TextView text_to_login;
    private Button btn_regis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        editText_email = findViewById(R.id.editText_email);
        editText_username = findViewById(R.id.editText_username);
        editText_password = findViewById(R.id.editText_password);
        editText_confirmpassword = findViewById(R.id.editText_confirmpassword);
        btn_regis = findViewById(R.id.btn_regis);


        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText_email.getText().toString().trim();
                String user = editText_username.getText().toString().trim();
                String password = editText_password.getText().toString().trim();
                String confirmpassword = editText_confirmpassword.getText().toString().trim();

                if (email.isEmpty() || user.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
                    // Tampilkan pesan kesalahan jika ada field yang kosong
                    Toast.makeText(register.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                } else {
                    // Lakukan autentikasi menggunakan Firebase Authentication
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Registrasi berhasil
                                Toast.makeText(register.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                                // Pindah ke LoginActivity
                                Intent intent = new Intent(register.this, login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Registrasi gagal
                                Toast.makeText(register.this, "Registrasi gagal: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        TextView textToLogin = findViewById(R.id.text_to_login);
        // Tambahkan OnClickListener pada text_to_login
        textToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke LoginActivity
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
            }
        });
    }
}