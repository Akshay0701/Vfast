package com.example.vfast.customerPages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.vfast.MainActivity;
import com.example.vfast.Model.ModelUser;
import com.example.vfast.R;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.rtchagas.pingplacepicker.PingPlacePicker;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class AddOrder extends AppCompatActivity  {

    //map
    private static final int REQUEST_LOCATION = 1;
    Button btnGetLocation;
    TextView showLocation;
    LocationManager locationManager;
    String latitude, longitude;

    String Name = "";

    private final int PickUP_PhoneCode=99;
    private final int EndUP_PhoneCode=100;
    private final int EndUP_PlacePicker=101;
    private final int PickUp_PlacePicker=102;

    //process dialog
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Order");

    FirebaseUser user;

    EditText NameId,pickup_address_id,end_address_id,phone_id,weight,product,end_phone_id,pickup_date_id,pickup_time_id,pickup_comment,endup_time_id,endup_date_id,endup_comment;
    Button orderBtn;

    String namestr;

    ImageView pickup_contact,endup_contact,pickup_lcoation;

    //for suggestion  user
    Button productBooks,productDocs,productImp,weight2KG,weight3KG,weight5KG;

    private int mYear, mMonth, mDay, mHour, mMinute;

    String date,time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        //for location
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        pickup_lcoation=findViewById(R.id.pickup_lcoation);




        orderBtn=findViewById(R.id.addOrder);
        pickup_date_id=findViewById(R.id.pickup_date_id);
        pickup_time_id=findViewById(R.id.pickup_time_id);
        pickup_comment=findViewById(R.id.pickup_comment);
        endup_time_id=findViewById(R.id.endup_time_id);
        endup_date_id=findViewById(R.id.endup_date_id);
        endup_comment=findViewById(R.id.endup_comment);
        pickup_address_id=findViewById(R.id.pickup_address_id);
        end_address_id=findViewById(R.id.endup_address_id);
        phone_id=findViewById(R.id.pickup_phone_txt);
        weight=findViewById(R.id.weighttxt);
        product=findViewById(R.id.producttxt);
        end_phone_id=findViewById(R.id.end_phone_txt);
        pickup_contact=findViewById(R.id.pickup_contact);
        endup_contact=findViewById(R.id.endup_contact);

        //pick placex
        pickup_lcoation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:user place picker
                pickup_lcoation=findViewById(R.id.pickup_lcoation);
                Log.e("address","address");
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();

                    Log.e("address","address");
                } else {
                    getLocation();
                }
            }
        });


        productBooks=findViewById(R.id.productBooks);
        productDocs=findViewById(R.id.productDocs);
        productImp=findViewById(R.id.productImp);
        weight2KG=findViewById(R.id.weight2KG);
        weight3KG=findViewById(R.id.weight3KG);
        weight5KG=findViewById(R.id.weight5KG);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        if (user==null){
            finish();
        }
        getNameUser();

        productBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.setText(productBooks.getText().toString());
            }
        });
        productDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.setText(productDocs.getText().toString());
            }
        });
        productImp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.setText(productImp.getText().toString());
            }
        });
        weight2KG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weight.setText(weight2KG.getText().toString());
            }
        });
        weight3KG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weight.setText(weight3KG.getText().toString());
            }
        });
        weight5KG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weight.setText(weight5KG.getText().toString());
            }
        });

        progressDialog=new ProgressDialog(this);

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pickup,endup,phone,endphone,weightst,products,pickdate,picktime,pickcomment,enddate,endtime,endcomment;
                endcomment="  ";
                pickcomment="  ";
                pickcomment=pickup_comment.getText().toString();
                pickdate=pickup_date_id.getText().toString();
                picktime=pickup_time_id.getText().toString();
                endcomment=endup_comment.getText().toString();
                enddate=endup_date_id.getText().toString();
                endtime=endup_time_id.getText().toString();
              //  name=NameId.getText().toString();
                pickup=pickup_address_id.getText().toString();
                endup=end_address_id.getText().toString();
                phone=phone_id.getText().toString();
                weightst=weight.getText().toString();
                products=product.getText().toString();
                endphone=end_phone_id.getText().toString();

                if (!Patterns.PHONE.matcher(phone).matches()){
                    phone_id.setError("Invalid Phone No");
                    phone_id.setFocusable(true);
                   // Toast.makeText(CustomerRegister.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.PHONE.matcher(endphone).matches()){
                    end_phone_id.setError("Invalid Phone No");
                    //Toast.makeText(CustomerRegister.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                    end_phone_id.setFocusable(true);
                }
                else if(pickup.equals("")){
                    pickup_address_id.setError("Please Fill Address");
                    pickup_address_id.setFocusable(true);
                }
                else if(endup.equals("")){
                    end_address_id.setError("Please Fill Address");
                    end_address_id.setFocusable(true);
                }
                else if(weightst.equals("")){
                    weight.setError("Please Fill Weight");

                   // Toast.makeText(CustomerRegister.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                    weight.setFocusable(true);
                }
                else if(products.equals("")){
                    product.setError("Please Fill Product Name");
                    // Toast.makeText(CustomerRegister.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                    product.setFocusable(true);
                }
                else if(enddate.equals("")){
                    endup_date_id.setError("Please Fill ");
                    // Toast.makeText(CustomerRegister.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                    endup_date_id.setFocusable(true);
                }
                else if(pickdate.equals("")){
                    pickup_date_id.setError("Please Fill ");
                    // Toast.makeText(CustomerRegister.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                    pickup_date_id.setFocusable(true);
                }
                else if(picktime.equals("")){
                    pickup_time_id.setError("Please Fill ");
                    // Toast.makeText(CustomerRegister.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                    pickup_time_id.setFocusable(true);
                }
                else if(endtime.equals("")){
                    endup_time_id.setError("Please Fill ");
                    // Toast.makeText(CustomerRegister.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                    endup_time_id.setFocusable(true);
                }
                else {
                    OrderPlace(pickup_address_id.getText().toString(),end_address_id.getText().toString()
                            ,phone_id.getText().toString(),weight.getText().toString(),product.getText().toString()
                            ,end_phone_id.getText().toString(),pickdate,picktime,pickcomment,enddate,endtime,endcomment);

                }
            }
        });

        endup_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(AddOrder.this)
                        .withPermission(android.Manifest.permission.READ_CONTACTS)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                            }
                        })
                        .check();
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, EndUP_PhoneCode);
            }
        });
        pickup_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                Dexter.withContext(AddOrder.this)
                        .withPermission(android.Manifest.permission.READ_CONTACTS)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                            }
                        })
                        .check();
                startActivityForResult(intent, PickUP_PhoneCode);
            }
        });


        //getting time and date
        pickup_date_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b){
                    getDate(pickup_date_id);
                }
            }


        });
        //getting time and date
        pickup_time_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    // Get Current Time
                   getTime(pickup_time_id);
                }
            }
        });
        //getting time and date
        endup_date_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    getDate(endup_date_id);
                    endup_date_id.setText(date);
                }
            }
        });
        //getting time and date
        endup_time_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    getTime(endup_time_id);
                    endup_time_id.setText(date);
                }
            }
        });
        //getting time and date
        pickup_date_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    getDate(pickup_date_id);
                    pickup_date_id.setText(date);
                }
            }
        });




   }


   //for map
   private void OnGPS() {
       final AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
           }
       }).setNegativeButton("No", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
           }
       });
       final AlertDialog alertDialog = builder.create();
       alertDialog.show();
   }
    private void getLocation() {
        Log.e("address","address");
        if (ActivityCompat.checkSelfPermission(
                AddOrder.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                AddOrder.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                //showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(AddOrder.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                String addres=address+city+state+country+postalCode+knownName;
                Log.e("address","address:"+address+city+state+country+postalCode+knownName);
                if (pickup_address_id==null){
                    return;
                }
                pickup_address_id.setText(addres);

            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setText(String str, EditText view) {
        view.setText(str);
    }

    void getTime(final EditText views){
       // Get Current Time
       final Calendar c = Calendar.getInstance();
       mHour = c.get(Calendar.HOUR_OF_DAY);
       mMinute = c.get(Calendar.MINUTE);

       // Launch Time Picker Dialog
       TimePickerDialog timePickerDialog = new TimePickerDialog(this,AlertDialog.THEME_HOLO_DARK,
               new TimePickerDialog.OnTimeSetListener() {

                   @Override
                   public void onTimeSet(TimePicker view, int hourOfDay,
                                         int minute) {

                      views.setText( hourOfDay + ":" + minute);
                   }
               }, mHour, mMinute, false);
       timePickerDialog.show();

   }
   void getDate(final EditText views){
       //get date for pick time
       // Get Current Date
       final Calendar c = Calendar.getInstance();
       mYear = c.get(Calendar.YEAR);
       mMonth = c.get(Calendar.MONTH);
       mDay = c.get(Calendar.DAY_OF_MONTH);


       DatePickerDialog datePickerDialog = new DatePickerDialog(AddOrder.this, AlertDialog.THEME_HOLO_DARK,
               new DatePickerDialog.OnDateSetListener() {

                   @Override
                   public void onDateSet(DatePicker view, int year,
                                         int monthOfYear, int dayOfMonth) {
                  date=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                       views.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                   }
               }, mYear, mMonth, mDay);
       datePickerDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PickUP_PhoneCode):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                phone_id.setText(num);
                               // Toast.makeText(AddOrder.this, "Number="+num, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    break;
                }
            case (EndUP_PhoneCode):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                end_phone_id.setText(num);
                                // Toast.makeText(AddOrder.this, "Number="+num, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    break;
                }

        }
    }


    private void getNameUser() {
        String uid =user.getUid();
        Query query =FirebaseDatabase.getInstance().getReference("Users").orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    //data
                    ModelUser user=ds.getValue(ModelUser.class);
                    namestr=user.getName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void OrderPlace(String pickup, String endup, String phone, String weight, String product, String endPhone, String pickdate, String picktime, String pickcomment, String enddate, String endtime, String endcomment) {
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Users");
        progressDialog.setMessage("Order Placing..");
        progressDialog.show();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String orderKey = database.getReference("quiz").push().getKey();

        final HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("User_Name",namestr);
        hashMap.put("PickUp_Address",pickup);
        hashMap.put("EndUp_Address",endup);
        hashMap.put("PickUp_phone",phone);
        hashMap.put("EndAddPhone",endPhone);
        hashMap.put("ProductWeight",weight);
        hashMap.put("uid",user.getUid());
        hashMap.put("User_Email",user.getEmail());
        hashMap.put("Product",product);
        hashMap.put("ProductKey",orderKey);
        hashMap.put("PickUpDate",pickdate);
        hashMap.put("PickUpTime",picktime);
        hashMap.put("PickUpComment",pickcomment);
        hashMap.put("EndUpDate",enddate);
        hashMap.put("EndUpTime",endtime);
        hashMap.put("EndUpComment",endcomment);
        hashMap.put("Status","-1");//this is status it will reprenst by process 0 at start 1 when allocate 2 when deleboy get to pickup 3 when deleboy send to endup
        hashMap.put("Allocated_DeleID","Not Yet Allocated");
        hashMap.put("ProductPrice","0");
        hashMap.put("DeleComment","sample");
        ref.child(orderKey).setValue(hashMap);
        progressDialog.dismiss();

        //sucess
        Toast.makeText(AddOrder.this, "Order Placed"+user.getEmail(), Toast.LENGTH_SHORT).show();

      //  startActivity(new Intent(AddOrder.this, Cus.class));
        finish();


    }


}


