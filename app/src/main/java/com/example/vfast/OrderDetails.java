package com.example.vfast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vfast.Delevery.DeleveryMain;
import com.example.vfast.Model.ModelDeleBoy;
import com.example.vfast.Model.ModelOrders;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OrderDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    FirebaseAuth firebaseAuth;
    String mUID;
    FirebaseUser user;

   boolean isDelereyBoy=false;
   String Productkey="";
    //process dialog
    ProgressDialog progressDialog;

    ModelOrders modelOrders;
    FirebaseAuth mAuth;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Order");


    Button AcceptOrderBtn;
    //this is view of order
    TextView Unametv,pricetxt,pickup_address_id,pickup_phone_txt,endup_address_id,end_phone_txt,productKeytxt,producttxt,statustxt,weighttxt;

    //this is view of dele boy
    TextView isDeleAllocatedTxt,Delenametv,DelePhonetxt,DeleEmailtxt;

    Spinner spinner;

    //its current product stats
    String ProductStatus="0";

    CardView deleLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);


        deleLinear=findViewById(R.id.deleLinear);


        Intent intent = getIntent();
        try {
            isDelereyBoy=intent.getBooleanExtra("isDeleBoy",false);
            Productkey=intent.getStringExtra("productKey");
            Toast.makeText(this, ""+Productkey, Toast.LENGTH_SHORT).show();
        }catch (NullPointerException e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //setting view of product
        Unametv=findViewById(R.id.Unametv);
        pricetxt=findViewById(R.id.pricetxt);
        pickup_address_id=findViewById(R.id.pickup_address_id);
        pickup_phone_txt=findViewById(R.id.pickup_phone_txt);
        endup_address_id=findViewById(R.id.endup_address_id);
        end_phone_txt=findViewById(R.id.end_phone_txt);
        productKeytxt=findViewById(R.id.productKeytxt);
        producttxt=findViewById(R.id.producttxt);
        statustxt=findViewById(R.id.statustxt);
        weighttxt=findViewById(R.id.weighttxt);

        //setting view dele boy
        isDeleAllocatedTxt=findViewById(R.id.isDeleAllocatedTxt);
        Delenametv=findViewById(R.id.Delenametv);
        DelePhonetxt=findViewById(R.id.DelePhonetxt);
        DeleEmailtxt=findViewById(R.id.DeleEmailtxt);

        loadOrderDetails();

        //product detail

     //   allocatedDele=findViewById(R.id.allocatedDele);
       // textStatus=findViewById(R.id.textStatus);


    }

    private void loadOrderDetails() {
    //load details
        Query query =FirebaseDatabase.getInstance().getReference("Order").orderByChild("ProductKey").equalTo(Productkey);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    modelOrders =ds.getValue(ModelOrders.class);
                    String status=modelOrders.getStatus();
                    producttxt.setText("Products :"+modelOrders.getProduct());
                    Unametv.setText(modelOrders.getUser_Name());
                    pickup_address_id.setText(modelOrders.getPickUp_Address());
                    endup_address_id.setText(modelOrders.getEndUp_Address());
                    pickup_phone_txt.setText(modelOrders.getPickUp_phone());
                    weighttxt.setText("Weight :"+modelOrders.getProductWeight());
                    end_phone_txt.setText(modelOrders.getEndAddPhone());
                    //.setText(modelOrders.getAllocated_DeleID());
                    if (!modelOrders.getProductPrice().equals("0")){
                        pricetxt.setText("Price :"+modelOrders.getProductPrice()+"₹");
                    }else{
                        pricetxt.setText("Price Not Decided Yet");
                    }

                    //checking status of product
                    if (status.equals("0")){
                        statustxt.setText("Searching For Delivery Boy");
                    }
                    else if(status.equals("1")){
                        statustxt.setText("On The Way");
                    }else if(status.equals("2")){
                        statustxt.setText("Picked Product From location");
                    }else if(status.equals("3")){
                        statustxt.setText("On The Way");
                    }else if(status.equals("4")){
                        statustxt.setText("Drop To End Location");
                    }


                    //checking if already alllocated or not
                    String allocated_deleBoy=modelOrders.getAllocated_DeleID();
                    if (!modelOrders.getAllocated_DeleID().equals("Not Yet Allocated")) {
                        //here uesr will get dele boy details
                        isDeleAllocatedTxt.setText("****Allocated Delivery Boy****");
                        deleLinear.setVisibility(View.VISIBLE);
                        //get deleboy details
                        Query query =FirebaseDatabase.getInstance().getReference("DeleveryBoy").orderByChild("uid").equalTo(allocated_deleBoy);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds:dataSnapshot.getChildren()){
                                    ModelDeleBoy deleBoy=ds.getValue(ModelDeleBoy.class);
                                    Delenametv.setText(deleBoy.getName());
                                    DeleEmailtxt.setText(deleBoy.getEmail());
                                    DelePhonetxt.setText(deleBoy.getPhone());

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        isDeleAllocatedTxt.setText("****Searching For Delivery Boy****");
                        deleLinear.setVisibility(View.GONE);
                        //do nothing
                    }
                }
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


            //updatetoken
            //noinspection deprecation

        }
        else{
            startActivity(new Intent(OrderDetails.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();
        //we are setting status
        if (text.equals("Picked Product From location")){
            ProductStatus="2";
            Toast.makeText(this, ""+ProductStatus, Toast.LENGTH_SHORT).show();
            updatestuatus(ProductStatus);
        }
        else if (text.equals("On The Way")){
            ProductStatus="3";
            Toast.makeText(this, ""+ProductStatus, Toast.LENGTH_SHORT).show();
            updatestuatus(ProductStatus);
        }
        else if (text.equals("Drop To End Location")){
            ProductStatus="4";
            Toast.makeText(this, ""+ProductStatus, Toast.LENGTH_SHORT).show();
            updatestuatus(ProductStatus);
        }
        //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

    }

    private void updatestuatus(String productStatus) {
        if (ProductStatus!=null&&modelOrders!=null) {
            ref.child(modelOrders.getProductKey()).child("Status").setValue(productStatus);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
