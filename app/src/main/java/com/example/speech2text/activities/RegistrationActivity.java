package com.example.speech2text.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.speech2text.R;
import com.example.speech2text.models.Role;
import com.example.speech2text.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity {
    EditText username,password,firstname,lastname,email;
    Button singup,login;
    FirebaseAuth mAuth ;
    FirebaseFirestore firestore;
    String USER_KEY = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        singup.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                         String Email = email.getText().toString();
                         String Password = password.getText().toString();
                         String Firstname = firstname.getText().toString();
                         String Lastname  = lastname.getText().toString();
                         String Username = username.getText().toString();

                        mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegistrationActivity.this, "sign in error", Toast.LENGTH_SHORT).show();
                                } else{
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    String id = currentUser.getUid();
                                    System.out.println(id+"-------------------------------------------------------------");

                                    User user = new User(id,Firstname,Lastname,Username,Email, Role.USER);
                                    firestore.collection(USER_KEY).document(user.getEmail()).set(user).addOnSuccessListener(RegistrationActivity.this, new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
//                                            System.out.println("added to firestore ---------------------------------------------------------");
//                                            Toast.makeText(RegistrationActivity.this,"Added to firestore!",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),MainTranslateActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    Toast.makeText(RegistrationActivity.this, "successfully created!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private void init(){
        login = findViewById(R.id.login);
        singup = findViewById(R.id.singup);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }
}