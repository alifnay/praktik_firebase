package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class register extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private EditText editText_email, editText_username, editText_password, editText_confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        editText_email = findViewById(R.id.editText_email);
        editText_username = findViewById(R.id.editText_username);
        editText_password = findViewById(R.id.editText_password);
        editText_confirmpassword = findViewById(R.id.editText_confirmpassword);
        Button btn_regis = findViewById(R.id.btn_regis);

        btn_regis.setOnClickListener(v -> {
            String email = editText_email.getText().toString().trim();
            String user = editText_username.getText().toString().trim();
            String password = editText_password.getText().toString().trim();
            String confirmpassword = editText_confirmpassword.getText().toString().trim();

            if (email.isEmpty() || user.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
                Toast.makeText(register.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Menyimpan data pengguna ke Firestore
                        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("username", user);
                        userMap.put("email", email);

                        firestore.collection("users").document(userId)
                                .set(userMap)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(register.this, "Registrasi dan penyimpanan data berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(register.this, login.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(register.this, "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(register.this, "Registrasi gagal: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        TextView textToLogin = findViewById(R.id.text_to_login);
        textToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(register.this, login.class);
            startActivity(intent);
        });
    }
}
