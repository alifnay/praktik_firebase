package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText editText_email, editText_password;
    private Button btn_login;
    private TextView text_to_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText_email.getText().toString().trim();
                String password = editText_password.getText().toString().trim();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!password.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(login.this, MainActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        editText_password.setError("Empty fields are not allowed");
                    }
                } else if (email.isEmpty()) {
                    editText_email.setError("Empty fields are not allowed");
                } else {
                    editText_email.setError("Please enter correct email");
                }
            }
        });

        TextView textToRegister = findViewById(R.id.text_to_register);
        // Tambahkan OnClickListener pada text_to_login
        textToRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Pindah ke LoginActivity
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
            }
        });
    }
}