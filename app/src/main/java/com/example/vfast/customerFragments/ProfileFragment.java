package com.example.vfast.customerFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
public class ProfileFragment extends Fragment {



    FirebaseAuth firebaseAuth;
    String mUID;
    FirebaseUser user;

    TextView Unametv,phonetv;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        phonetv=view.findViewById(R.id.phonetv);
        Unametv=view.findViewById(R.id.Unametv);
        checkforuserlogin();
        return view;
    }

    public void checkforuserlogin() {
        firebaseAuth= FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user != null) {

            mUID=user.getUid();

            Query query= FirebaseDatabase.getInstance().getReference("Users").orderByChild("uid").equalTo(mUID);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        ModelUser user=ds.getValue(ModelUser.class);
                        Unametv.setText(user.getName());
                        phonetv.setText(user.getPhone());
                    }
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
