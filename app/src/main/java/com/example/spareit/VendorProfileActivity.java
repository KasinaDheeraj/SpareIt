package com.example.spareit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class VendorProfileActivity extends AppCompatActivity {
    String name;
    String phoneNumber;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);

        getSupportActionBar().setTitle("Vendor Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            name=extras.getString("NAME");
            phoneNumber = extras.getString("PHONE");
            address = extras.getString("ADDRESS");
        }

        TextView nameTV=findViewById(R.id.vnameTV);
        TextView phoneTV=findViewById(R.id.phoneTV);
        TextView addressTV=findViewById(R.id.addressTV);

        nameTV.setText(name);
        phoneTV.setText(phoneNumber);
        addressTV.setText(address);

    }

    public void call(View v){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}