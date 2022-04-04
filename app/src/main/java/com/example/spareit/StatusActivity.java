package com.example.spareit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.spareit.adapters.RVItemAdapter;

import java.lang.reflect.Array;

public class StatusActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RVItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        getSupportActionBar().setTitle("Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // Set local attributes to corresponding views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_status_activity);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter=new RVItemAdapter(this,false);
        adapter.onlyThreshold();
        recyclerView.setAdapter(adapter);
        adapter.setListener(new RVItemAdapter.Listener() {
            @Override
            public void onClick(int position) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(StatusActivity.this, new String[]{android.Manifest.permission.CALL_PHONE},
                            0110);

                } else {
                    Intent intent=new Intent(StatusActivity.this,VendorProfileActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("NAME",adapter.items.get(position).vendorName);
                    extras.putString("PHONE",adapter.items.get(position).phoneNo);
                    extras.putString("ADDRESS",adapter.items.get(position).address);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }
        });

    }
}