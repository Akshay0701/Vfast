package com.example.vfast.customerFragments;

import android.content.Intent;
import android.net.Uri;
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
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment
     {



    FirebaseAuth firebaseAuth;
    String mUID;
    FirebaseUser user;
    PDFView pdfView;

    public ChatFragment() {
        // Required empty public constructor
    }
    TextView phoneNo,pdftxt;
    String pdflink="https://firebasestorage.googleapis.com/v0/b/vfast-b9d22.appspot.com/o/terms_and_conditions_for_flashd-converted.pdf?alt=media&token=84289c97-6e3b-42eb-82bd-649c49043135";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        phoneNo=view.findViewById(R.id.customer_careNo);
        checkforuserlogin();
        pdftxt=view.findViewById(R.id.pdftxt);
        phoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCallPhone();
            }
        });
        pdftxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(pdflink));
                startActivity(i);
            }
        });
        return view;
    }

         void openCallPhone(){
             Intent intent = new Intent(Intent.ACTION_DIAL);
             intent.setData(Uri.parse("tel:"+phoneNo.getText().toString()));
             startActivity(intent);
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
