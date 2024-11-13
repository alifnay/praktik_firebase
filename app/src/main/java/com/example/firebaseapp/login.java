package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class login extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private EditText editText_email, editText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        Button btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(v -> {
            String email = editText_email.getText().toString().trim();
            String password = editText_password.getText().toString().trim();

            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!password.isEmpty()) {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(authResult -> {
                                String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

                                // Mengambil data pengguna dari Firestore
                                firestore.collection("users").document(userId)
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            if (documentSnapshot.exists()) {
                                                String username = documentSnapshot.getString("username");
                                                String email1 = documentSnapshot.getString("email");

                                                // Tampilkan pesan atau simpan data untuk digunakan di MainActivity
                                                Toast.makeText(login.this, "Welcome, " + username, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(login.this, MainActivity.class);
                                                intent.putExtra("username", username);
                                                intent.putExtra("email", email1);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(login.this, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show());
                            }).addOnFailureListener(
                                    e -> Toast.makeText(login.this, "Login Gagal", Toast.LENGTH_SHORT).show());
                } else {
                    editText_password.setError("Password tidak boleh kosong");
                }
            } else if (email.isEmpty()) {
                editText_email.setError("Email tidak boleh kosong");
            } else {
                editText_email.setError("Format email tidak valid");
            }
        });

        TextView textToRegister = findViewById(R.id.text_to_register);
        textToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, register.class);
            startActivity(intent);
        });
    }
}
