package com.example.vfast.customerPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
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
import com.example.vfast.notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class CustomerMain extends AppCompatActivity {

    Button addOrder;

    //notification
    private static final String ID="some_id";
    private static final String NAME="FirebaseAPP";

    FirebaseAuth firebaseAuth;
    String mUID;
    FirebaseUser user;



    BottomNavigationView smoothBottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        smoothBottomBar=findViewById(R.id.bottomBar);
        checkforuserlogin();

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
                    case R.id.action_CustomerCare :
                        ChatFragment fragment5=new ChatFragment();
                        FragmentTransaction ft5=getSupportFragmentManager().beginTransaction();
                        ft5.replace(R.id.content1,fragment5,"");
                        //currentpos=0;
                        ft5.commit();
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
                }
                return false;
            }
        });
        createNotificationChannel();
    }



    private void createNotificationChannel() {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //setting user to particular category
        FirebaseMessaging.getInstance().subscribeToTopic("nonuser")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Done";
                        if (!task.isSuccessful()) {
                            msg = "Error";
                        }
                        Toast.makeText(CustomerMain.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel notificationChannel = new NotificationChannel(ID
                    ,NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(getString(R.string.CHANNEL_DESCRIPTION));
            notificationChannel.setShowBadge(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);


            if (defaultSoundUri != null) {
                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();
                notificationChannel.setSound(defaultSoundUri, att);
            }

            notificationManager.createNotificationChannel(notificationChannel);



            Toast.makeText(this, "created", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateToken(String token){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken=new Token(token);
        ref.child(mUID).setValue(mToken);
    }
    public void checkforuserlogin() {
        firebaseAuth= FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user != null) {

            mUID=user.getUid();
            SharedPreferences sp=getSharedPreferences("SP_User",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("Current_DELEVERYID",mUID);
            editor.apply();
            //noinspection deprecation
            updateToken(FirebaseInstanceId.getInstance().getToken());
            //updatetoken
            //noinspection deprecation

        }
        else{
            startActivity(new Intent(CustomerMain.this,Customer_Login.class));
            finish();
        }
    }




}

