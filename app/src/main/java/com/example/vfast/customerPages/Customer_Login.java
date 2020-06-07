package com.example.vfast.customerPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class Customer_Login extends AppCompatActivity {


    EditText log_emailEt,log_passwordEt;
    ImageView login_submit;
    TextView recover_pass,not_already_account;

    ProgressDialog progressDialog;


    FirebaseAuth mAuth;

    //auto login
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__login);
        getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this,4);
        editor= PreferenceManager.getDefaultSharedPreferences(this).edit();

        log_emailEt=findViewById(R.id.log_emailEt);
        log_passwordEt=findViewById(R.id.log_passwordEt);

        login_submit=findViewById(R.id.login_submit);
        recover_pass=findViewById(R.id.recover_pass);
        not_already_account=findViewById(R.id.not_already_account);

        //go back
        not_already_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Customer_Login.this, CustomerRegister.class));
            }
        });

        recover_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showrecoveryAlreatbox();

            }
        });

        login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check is enter
                String email=log_emailEt.getText().toString().trim();
                String password=log_passwordEt.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    log_emailEt.setError("Invalided Email");
                    log_emailEt.setFocusable(true);

                }
                else if(password.length()<6){
                    log_passwordEt.setError("Password length at least 6 characters");
                    log_passwordEt.setFocusable(true);
                }
                else {
                    //allow to eneter
                    editor.putString("username", email.trim());
                    editor.putString("password", password.trim());
                    editor.apply();
                    login(email,password);

                }

            }
        });


    }

    //recover pass

    private void showrecoveryAlreatbox() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this,4);
        builder.setTitle("Recover Password");

        //set layout  linear layout
        LinearLayout linearLayout= new LinearLayout(this);

        final EditText emailEt=new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailEt);
        emailEt.setMinEms(16);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailEt.getText().toString().trim();

                beginrecover(email);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
            }
        });

        builder.create().show();

    }

    private void beginrecover(String email) {
        progressDialog.setMessage("Logging...");
        progressDialog.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Customer_Login.this, "email sent", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Customer_Login.this, "failed..", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Customer_Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                startActivity(new Intent(Customer_Login.this, CustomerMain.class));
                                finish();


                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                                Toast.makeText(Customer_Login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Log.e("aksahy",e.getMessage());
                    Toast.makeText(Customer_Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        super.onStart();
    }

    //go back

    public boolean onSupportNavigateUp(){
        onBackPressed();//go baack
        return super.onSupportNavigateUp();
    }


    private void login(String email, String password) {
        progressDialog.setMessage("Logging...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Customer_Login.this, "logend susscess", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Customer_Login.this, CustomerMain.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(Customer_Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Customer_Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

}

