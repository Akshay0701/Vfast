package com.example.vfast.customerFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vfast.MainActivity;
import com.example.vfast.Model.ModelUser;
import com.example.vfast.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {



    FirebaseAuth firebaseAuth;
    String mUID;
    FirebaseUser user;

    public ChatFragment() {
        // Required empty public constructor
    }
    TextView phoneNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        phoneNo=view.findViewById(R.id.customer_careNo);
        checkforuserlogin();
        return view;
    }


    public void checkforuserlogin() {
        firebaseAuth= FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user != null) {

            mUID=user.getUid();

            Query query= FirebaseDatabase.getInstance().getReference("CustomerCare");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   // Log.e("aaa", dataSnapshot.child("Phone").getValue().toString());
                    phoneNo.setText(dataSnapshot.child("Phone").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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
