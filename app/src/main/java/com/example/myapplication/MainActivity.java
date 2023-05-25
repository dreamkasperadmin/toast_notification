package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button allowPermission;
    private  Button makeToastBtn;
    private  Button apiTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allowPermission = findViewById(R.id.allowPermission);
        makeToastBtn = findViewById(R.id.makeToast);
        apiTest = findViewById(R.id.apiTest);
        allowPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
        });
        makeToastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Welcome to Toast Detection", Toast.LENGTH_SHORT).show();
            }
        });
        apiTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSendPostRequestClicked();
            }
        });
    }

    private void btnSendPostRequestClicked(){
        try {
            ApiInterface apiInterface = RetrofitClient.getRetroFitInstance().create(ApiInterface.class);
            Message message = new Message("Hello World", "myApp");
            Call<Void> call = apiInterface.sendMessage(message);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.e("API RESPONSE", "On RESPONSE"+ response.code());
                    Log.e("API RESPONSE", "On RESPONSE: MSG"+ response.body());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}