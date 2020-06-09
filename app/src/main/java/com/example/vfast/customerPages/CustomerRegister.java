package com.example.vfast.customerPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vfast.MainActivity;
import com.example.vfast.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CustomerRegister extends AppCompatActivity {



    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText mEmailtext,mpasswordtext,nameEt,phoneEt,confirm_passwordEt;
    TextView malreadytext;
    Button bregister;

    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog=new ProgressDialog(this,4);
        sharedPreferences=getSharedPreferences("Remember",MODE_PRIVATE);
        editor= PreferenceManager.getDefaultSharedPreferences(this).edit();
        mAuth = FirebaseAuth.getInstance();

        progressDialog.setMessage("Registering user...");


        mEmailtext=findViewById(R.id.emailEt);
        mpasswordtext=findViewById(R.id.passwordEt);
        nameEt=findViewById(R.id.nameEt);
        phoneEt=findViewById(R.id.phoneEt);
        confirm_passwordEt=findViewById(R.id.confirm_passwordEt);
        malreadytext=findViewById(R.id.already_account);
        bregister=findViewById(R.id.register_submit);



        //noinspection deprecation
        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=mEmailtext.getText().toString().trim();
                String password=mpasswordtext.getText().toString().trim();
                String confirm_password=confirm_passwordEt.getText().toString().trim();
                String phone=phoneEt.getText().toString().trim();
                String name=nameEt.getText().toString().trim();


                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmailtext.setError("Invalid Email");
                    mEmailtext.setFocusable(true);
                    Toast.makeText(CustomerRegister.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<6){
                    mpasswordtext.setError("Password length at least 6 characters");
                    mpasswordtext.setFocusable(true);
                }
                else if(!confirm_password.equals(password)){
                    mpasswordtext.setError("Password Doesn't Match");
                }
                else if(name.length()<15){
                    nameEt.setError("Name length should less than 15 characters");

                 //   Toast.makeText(CustomerRegister.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                    nameEt.setFocusable(true);
                }
                else if(phone.length()<10){
                    phoneEt.setError("Invalid Phone No");

            //        Toast.makeText(CustomerRegister.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                    phoneEt.setFocusable(true);
                }
                else {
                    editor.putString("username",email.trim());
                    editor.putString("password",password.trim());
                    editor.apply();
                    registerUser(email,password,nameEt.getText().toString(),phone);

                }
            }
        });

        malreadytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerRegister.this,Customer_Login.class));
                finish();
            }
        });




    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }


    private void registerUser(String email, final String password, final String name, final String phone) {


        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();

                            String email= user.getEmail();
                            String uid=user.getUid();
                            HashMap<Object, String> hashMap=new HashMap<>();

                            hashMap.put("Email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("Name",name);
                            hashMap.put("Phone",phone);
                            hashMap.put("Password",password);
                            hashMap.put("image","https://www.biowritingservice.com/wp-content/themes/tuborg/images/Executive%20Bio%20Sample%20Photo.png");
                            FirebaseDatabase database=FirebaseDatabase.getInstance();

                            DatabaseReference reference=database.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(CustomerRegister.this, "Registered with "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CustomerRegister.this, CustomerMain.class));
                            finish();

                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(CustomerRegister.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(CustomerRegister.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



    @Override
    protected void onStart() {
        //check if user already register

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String username=prefs.getString("username","");
        String pass=prefs.getString("password","");

        if(username.equals("")&&pass.equals("")) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setMessage("Logging...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(username, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(CustomerRegister.this, CustomerMain.class));
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                                Toast.makeText(CustomerRegister.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(CustomerRegister.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        super.onStart();
    }

    public boolean onSupportNavigateUp(){

        onBackPressed();//go baack

        return super.onSupportNavigateUp();
    }

}
