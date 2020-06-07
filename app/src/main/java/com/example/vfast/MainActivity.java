package com.example.vfast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vfast.Delevery.DeleveryRegister;
import com.example.vfast.Delevery.DeleveyLogin;
import com.example.vfast.customerPages.Customer_Login;

public class MainActivity extends AppCompatActivity {

    Button cutomer,delevery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cutomer =findViewById(R.id.customer);
        //delevery =findViewById(R.id.delevery);
        //chaneg pages
        cutomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Customer_Login.class));
            }
        });
       // delevery.setOnClickListener(new View.OnClickListener() {
      ///      @Override
     //       public void onClick(View view) {
      //          startActivity(new Intent(MainActivity.this, DeleveyLogin.class));
     //       }
     //   });


    }
}
