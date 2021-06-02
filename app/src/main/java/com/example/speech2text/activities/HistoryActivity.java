package com.example.speech2text.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.speech2text.R;
import com.example.speech2text.interfaces.HistoryService;
import com.example.speech2text.interfaces.TranslateService;
import com.example.speech2text.models.History;
import com.example.speech2text.models.TranslatedText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    LinearLayout login,registration,history,logout;
    FirebaseAuth mAuth ;
    ListView listView;
    ArrayList<String> histories;
    ArrayAdapter adapter;
    List<History> historyObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
        checkAuthState();
        FirebaseUser user = getCurrentUser();
        getUserHistory(user.getEmail());
        setOnclickItem();
//        ArrayList<String> test = new ArrayList<>();
//        test.add("hello");
//        test.add("world");
//        adapter = new ArrayAdapter<>(HistoryActivity.this, android.R.layout.simple_list_item_1,test);
//        listView.setAdapter(adapter);



    }

    private void init(){
        registration = findViewById(R.id.registration);
        logout = findViewById(R.id.logout);
        history = findViewById(R.id.history);
        drawerLayout = findViewById(R.id.drawer_layout);
        login = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.listView);
        histories = new ArrayList<>();
    }

    private void setOnclickItem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("------------------");

                History history = historyObjects.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                builder.setTitle("History");
                builder.setMessage("id : "+history.getId()+
                                    "\nText to translate: "+history.getTextToTranslate()+
                                    "\nFrom: "+history.getFromLanguage()+
                                    "\nTo: "+history.getToLanguage()+
                                    "\nTranslated text: "+history.getTranslatedText());
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                });
                builder.show();

            }
        });
    }

    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }
    public void getUserHistory(String email){
        if(email == null) email = "none";

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        HistoryService history = retrofit.create(HistoryService.class);
        Call<List<History>> tResult = history.getUserHistory(email);
        tResult.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                if(!response.isSuccessful()){
                    System.out.println("--------------------------");
                    System.out.println(response.code());
                }

                historyObjects =  response.body();
                for (History temp: historyObjects ){
                    System.out.println(temp.getTextToTranslate());
                    histories.add(temp.getTextToTranslate());
                }
                System.out.println("-------------------------------");
                adapter = new ArrayAdapter<>(HistoryActivity.this, android.R.layout.simple_list_item_1,histories);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                System.out.println(t.getMessage());
            }

        });
    }

    public void ClickMenu(View view){
        MainTranslateActivity.openDrawer(drawerLayout);
    }

    public void ClickMain(View view){
        MainTranslateActivity.
        redirectActivity(this,MainTranslateActivity.class);
    }

    public void ClickLogout(View view){
        MainTranslateActivity.logout(this);
    }

    public void ClickHistory(View view){
        MainTranslateActivity.redirectActivity(this,HistoryActivity.class);
    }

    public void ClickRegistration(View view){
        MainTranslateActivity.redirectActivity(this,RegistrationActivity.class);
    }

    public void ClickLogin(View view){
        MainTranslateActivity.redirectActivity(this,LoginActivity.class);
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

    @Override
    protected void onPause() {
        super.onPause();
        MainTranslateActivity.closeDrawer(drawerLayout);
    }
}