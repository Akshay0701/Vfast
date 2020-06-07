package com.example.vfast.Delevery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.vfast.Adapter.AdapterOrders;
import com.example.vfast.CustomDialogBox.DialogDeleMyOrder;
import com.example.vfast.MainActivity;
import com.example.vfast.Model.ModelOrders;
import com.example.vfast.R;
import com.example.vfast.customerPages.CustomerMain;
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

public class DeleveryMain extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    String mUID;
    FirebaseUser user;

    public RecyclerView recyclerview_order;
    public RecyclerView.LayoutManager layoutManager;
    List<ModelOrders> ordersList;
    //adapter need FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    AdapterOrders adapterOrder;
    FirebaseDatabase database;
    DatabaseReference requests;

    //his order will display here if  its didn;t get display then i don;t know  the ; error you have ;)
    RecyclerView hisrecyclerView;

    //for showing his allocated orders
    ImageView show_myorders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delevery_main);
        recyclerview_order=findViewById(R.id.recyclerview_order);

        //init Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Order");
        ordersList= new ArrayList<>();
        recyclerview_order.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(DeleveryMain.this);
        recyclerview_order.setLayoutManager(layoutManager);
        show_myorders=findViewById(R.id.show_myorders);
        loadOrders();

        show_myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show dialog that have his orders
               // DialogDeleMyOrder dialogDeleMyOrder=new DialogDeleMyOrder(DeleveryMain.this,4);
               // dialogDeleMyOrder.setCanceledOnTouchOutside(false);
              //  dialogDeleMyOrder.show();

                final AlertDialog.Builder builder=new AlertDialog.Builder(DeleveryMain.this,4);
                LayoutInflater inflater=LayoutInflater.from(DeleveryMain.this);
                View dialogView=inflater.inflate(R.layout.dialog_deleboyorder,null);
                hisrecyclerView=dialogView.findViewById(R.id.alret_recycler);
                builder.setView(dialogView);
                loadDeleverBoyOrders();
                      //for cancel
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                    }
                });

                builder.show();

            }
        });

    }

    private void loadDeleverBoyOrders() {
        FirebaseAuth auth =FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        if (user!=null){
            Query query=requests.orderByChild("Allocated_DeleID").equalTo(user.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ordersList.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        ModelOrders modelOrders=ds.getValue(ModelOrders.class);

                        ordersList.add(modelOrders);
                    }

                    adapterOrder = new AdapterOrders(DeleveryMain.this, ordersList,false);

                    hisrecyclerView.setLayoutManager(new LinearLayoutManager(DeleveryMain.this, LinearLayoutManager.VERTICAL, true));

                    //set adapter to recycle
                    hisrecyclerView.setAdapter(adapterOrder);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }


    private void loadOrders()
    {

        requests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelOrders modelOrders=ds.getValue(ModelOrders.class);

                    ordersList.add(modelOrders);
                }

                adapterOrder = new AdapterOrders(DeleveryMain.this, ordersList,false);

                recyclerview_order.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true));

                //set adapter to recycle
                recyclerview_order.setAdapter(adapterOrder);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        checkforuserlogin();
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

            //updatetoken
            //noinspection deprecation

        }
        else{
            startActivity(new Intent(DeleveryMain.this, MainActivity.class));
            finish();
        }
    }
}
