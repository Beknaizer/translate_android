package com.example.speech2text.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.speech2text.R;
import com.example.speech2text.interfaces.TranslateService;
import com.example.speech2text.models.TranslatedText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainTranslateActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    LinearLayout login,registration,history,logout;
    Spinner spinner1,spinner2;
    FirebaseAuth mAuth ;
    Button translate;
    EditText target;
    TextView result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_translate);
        init();
        checkAuthState();

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(MainTranslateActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.languages));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(myAdapter);
        spinner1.setAdapter(myAdapter);

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
                String targetText = target.getText().toString();
                String toLang =spinner2.getSelectedItem().toString();
                String fromLang =spinner1.getSelectedItem().toString();
                FirebaseUser user = mAuth.getCurrentUser();
                System.out.println(targetText+" "+toLang+" "+fromLang);
                try {
                    translate(targetText,toLang,fromLang,user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void init(){
        spinner1 = findViewById(R.id.lang1);
        spinner2 = findViewById(R.id.lang2);
        target = findViewById(R.id.target);
        result = findViewById(R.id.result);
        registration = findViewById(R.id.registration);
        logout = findViewById(R.id.logout);
        history = findViewById(R.id.history);
        translate =findViewById(R.id.translateButton);
        drawerLayout = findViewById(R.id.drawer_layout);
        login = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();

    }

    public void checkAuthState(){
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @org.jetbrains.annotations.NotNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    logout.setVisibility(View.VISIBLE);
                    history.setVisibility(View.VISIBLE);
                    login.setVisibility(View.GONE);
                    registration.setVisibility(View.GONE);
                }else{
                    login.setVisibility(View.VISIBLE);
                    registration.setVisibility(View.VISIBLE);
                    history.setVisibility(View.GONE);
                    logout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void translate(String input,String toLang,String fromLang,FirebaseUser user) throws IOException {
        String email;
        if(user == null) {
            email = "none";
        }else{
            email = user.getEmail();
        }


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        TranslateService translate = retrofit.create(TranslateService.class);
        Call<TranslatedText> tResult = translate.listRepos(email,input,fromLang,toLang);
        tResult.enqueue(new Callback<TranslatedText>() {
            @Override
            public void onResponse(Call<TranslatedText> call, Response<TranslatedText> response) {
                if(!response.isSuccessful()){
                    System.out.println(response.code());
                    return;
                }

                TranslatedText res = response.body();
                System.out.println(res.getTranslatedText());
                result.setText(res.getTranslatedText());
            }

            @Override
            public void onFailure(Call<TranslatedText> call, Throwable t) {
                System.out.println("--------------------");
                System.out.println(t.getMessage());
                result.setText(t.getMessage());
            }
        });
    }


    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public void ClickMain(View view){
        redirectActivity(this,MainTranslateActivity.class);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickLogout(View view){
        logout(this);
    }

    public void ClickHistory(View view){
        redirectActivity(this,HistoryActivity.class);
    }

    public void ClickRegistration(View view){
        redirectActivity(this,RegistrationActivity.class);
    }

    public void ClickLogin(View view){
        redirectActivity(this,LoginActivity.class);
    }


    public static void logout(final Activity activity){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are sure that you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                auth.signOut();
                redirectActivity(activity,MainTranslateActivity.class);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }



    public static void redirectActivity(Activity activity,Class aClass) {
        Intent intent = new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}