package com.example.task91p;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class createActivity extends AppCompatActivity {

    // The entry point to the Places API.
    private PlacesClient placesClient;
    EditText name, phone, description, date, mlocation;
    Button save, getlocation;
    Double l1;
     Double l2;
    RadioButton lost, found;

    String item;
    Database DB;
    LocationManager locationManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng mLatLng = null;
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null && result.getResultCode() == RESULT_OK) {
                Toast.makeText(createActivity.this, "11111", Toast.LENGTH_SHORT).show();
                if (result.getData() != null) {
                    Toast.makeText(createActivity.this, "2222", Toast.LENGTH_SHORT).show();
                    Place place = Autocomplete.getPlaceFromIntent(result.getData());
                    mlocation.setText(place.getAddress());
                    if(null != place.getLatLng()) {
                        l1 = place.getLatLng().latitude;
                        l2 = place.getLatLng().longitude;
                    }
                    //double lg=place.getLatLng().latitude;

                }
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DB = new Database(this);
        setContentView(R.layout.activity_create);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        description = findViewById(R.id.discription);
        getlocation = findViewById(R.id.getlocation);
        date = findViewById(R.id.date);
        mlocation = findViewById(R.id.location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        Places.initialize(getApplicationContext(), "AIzaSyAcsG2HK3pmnkYYJtydVjOlRxhtJHqnyxw");
        placesClient = Places.createClient(this);

        mlocation.setFocusable(false);
        mlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(createActivity.this);
                startForResult.launch(intent);

            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(createActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(createActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(createActivity.this, "dsdsdsds", Toast.LENGTH_SHORT).show();
                    Getlocation();
                } else {
                    ActivityCompat.requestPermissions(createActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 44);

                }

            }
        });


        save = findViewById(R.id.save);
        lost = findViewById(R.id.radioButton);
        found = findViewById(R.id.radioButton2);
        lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = lost.getText().toString();
            }
        });
        found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = found.getText().toString();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean insert = DB.insertData(item, name.getText().toString(), phone.getText().toString(), description.getText().toString(), date.getText().toString(), mlocation.getText().toString(),l1,l2);
                if (insert == true) {
                    Toast.makeText(createActivity.this, "data saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(createActivity.this, "Data notsaved", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 获取当前定位位置
     */
    private void Getlocation() {
        ProgressDialog progressDialog = ProgressDialog.show(createActivity.this,"","正在获取当前位置信息",true);
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.LAT_LNG);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.newInstance(placeFields);

        // Get the likely places - that is, the businesses and other points of interest that
        // are the best match for the device's current location.
        @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult =
                placesClient.findCurrentPlace(request);
        placeResult.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
            @Override
            public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    FindCurrentPlaceResponse likelyPlaces = task.getResult();
                    // 获取到了位置
                    if (!likelyPlaces.getPlaceLikelihoods().isEmpty()) {
                        // 取第一个可用的位置
                        Place place = null;
                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            place = placeLikelihood.getPlace();
                        }
                        if (null == place) {
                            Toast.makeText(getApplicationContext(), "获取不到可用位置", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 只取第一个位置
                        mLatLng = place.getLatLng();
                        mlocation.setText(place.getAddress());
                        if(null != place.getLatLng()) {
                            l1 = place.getLatLng().latitude;
                            l2 = place.getLatLng().longitude;
                        }

                        Toast.makeText(getApplicationContext(), "获取位置信息成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "获取不到可用位置", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "获取不到可用位置", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

}