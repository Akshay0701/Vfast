package com.example.vfast.CustomDialogBox;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.vfast.Adapter.AdapterOrders;
import com.example.vfast.Delevery.DeleveryMain;
import com.example.vfast.Model.ModelOrders;
import com.example.vfast.R;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DialogDeleMyOrder extends Dialog  {
    public DialogDeleMyOrder(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DialogDeleMyOrder(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public Activity activity;
    public Dialog dialog;
    public Button cancel;
    RecyclerView recyclerview_order;

    public RecyclerView.LayoutManager layoutManager;
    List<ModelOrders> ordersList;
    //adapter need FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    AdapterOrders adapterOrder;
    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_deleboyorder);
        recyclerview_order=findViewById(R.id.alret_recycler);
        cancel=findViewById(R.id.cancel);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Order");
        ordersList= new ArrayList<>();
        recyclerview_order.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(activity);
        recyclerview_order.setLayoutManager(layoutManager);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        loadDeleverBoyOrders();

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

                    adapterOrder = new AdapterOrders(getContext(), ordersList,false);

                    recyclerview_order.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));

                    //set adapter to recycle
                    recyclerview_order.setAdapter(adapterOrder);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }


}
