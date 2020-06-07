package com.example.vfast.customerPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vfast.Adapter.AdapterOrders;
import com.example.vfast.Delevery.DeleveryMain;
import com.example.vfast.MainActivity;
import com.example.vfast.Model.ModelOrders;
import com.example.vfast.OrderDetails;
import com.example.vfast.R;
import com.example.vfast.customerFragments.ChatFragment;
import com.example.vfast.customerFragments.InfoFragment;
import com.example.vfast.customerFragments.OrderFragment;
import com.example.vfast.customerFragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerMain extends AppCompatActivity {

    Button addOrder;

    FirebaseAuth firebaseAuth;
    String mUID;
    FirebaseUser user;



    BottomNavigationView smoothBottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        smoothBottomBar=findViewById(R.id.bottomBar);


       // actionBar.setTitle("Home");
    //    actionBar.setTitle(Html.fromHtml("<font color='#43B54A'>Home </font>"));
        OrderFragment fragment1=new OrderFragment();
        FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content1,fragment1,"");
        //currentpos=0;
        ft1.commit();
        smoothBottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_order:
                        OrderFragment fragment1=new OrderFragment();
                        FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
                        ft1.replace(R.id.content1,fragment1,"");
                        //currentpos=0;
                        ft1.commit();
                        return true;
                    case R.id.action_chat:
                        ChatFragment fragment2=new ChatFragment();
                        FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                        ft2.replace(R.id.content1,fragment2,"");
                        //currentpos=0;
                        ft2.commit();
                        return true;
                    case R.id.action_new_order:
                        startActivity(new Intent(CustomerMain.this,AddOrder.class));
                        return true;
                    case R.id.action_profile:
                        ProfileFragment fragment4=new ProfileFragment();
                        FragmentTransaction ft4=getSupportFragmentManager().beginTransaction();
                        ft4.replace(R.id.content1,fragment4,"");
                        //currentpos=0;
                        ft4.commit();
                        return true;
                    case R.id.action_info:
                        InfoFragment fragment3=new InfoFragment();
                        FragmentTransaction ft3=getSupportFragmentManager().beginTransaction();
                        ft3.replace(R.id.content1,fragment3,"");
                        //currentpos=0;
                        ft3.commit();
                        return true;
                }
                return false;
            }
        });

    }







}

