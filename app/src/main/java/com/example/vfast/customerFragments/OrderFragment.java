package com.example.vfast.customerFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vfast.Adapter.AdapterOrders;
import com.example.vfast.MainActivity;
import com.example.vfast.Model.ModelOrders;
import com.example.vfast.R;
import com.example.vfast.customerPages.AddOrder;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {



    ImageView addOrder;

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

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_order, container, false);

        addOrder=view.findViewById(R.id.addOrder);

        recyclerview_order=view.findViewById(R.id.recyclerview_myOrder);

        //init Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Order");
        ordersList= new ArrayList<>();
        recyclerview_order.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerview_order.setLayoutManager(layoutManager);
        checkforuserlogin();
        loadOrders();


        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: goto add order page
                startActivity(new Intent(getContext(), AddOrder.class));
            }
        });

    return  view;
    }
    private void loadOrders()
    {
      //  Toast.makeText(getContext(), ""+user.getUid(), Toast.LENGTH_SHORT).show();
        Query query=requests.orderByChild("uid").equalTo(mUID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelOrders modelOrders=ds.getValue(ModelOrders.class);

                    ordersList.add(modelOrders);
                }

                adapterOrder = new AdapterOrders(getContext(), ordersList,true);

                recyclerview_order.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));

                //set adapter to recycle
                recyclerview_order.setAdapter(adapterOrder);

                //   recyclerview_order.notifyAll();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void checkforuserlogin() {
        firebaseAuth= FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user != null) {

            mUID=user.getUid();
         //   SharedPreferences sp=getSharedPreferences("SP_User",MODE_PRIVATE);
         //   SharedPreferences.Editor editor=sp.edit();
        //    editor.putString("Current_USERID",mUID);
         //   editor.apply();

            //updatetoken
            //noinspection deprecation

        }
        else{
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        }
    }


}
