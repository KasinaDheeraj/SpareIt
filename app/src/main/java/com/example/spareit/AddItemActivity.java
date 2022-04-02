package com.example.spareit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spareit.data.AppDatabase;
import com.example.spareit.data.Items;

public class AddItemActivity extends AppCompatActivity {
    EditText nameET;
    EditText countET;
    EditText vendorNameET;
    EditText vendorPhNoET;

    String uid=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        nameET=findViewById(R.id.nameET);
        countET=findViewById(R.id.countET);
        vendorNameET=findViewById(R.id.VnameET);
        vendorPhNoET=findViewById(R.id.VnumET);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            uid=extras.getString("uid");
            nameET.setText(extras.getString("Name"));
            countET.setText(String.valueOf(extras.getInt("count")));
            vendorNameET.setText(extras.getString("vendorName"));
            vendorPhNoET.setText(extras.getString("phoneNo"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.Save){

            String name=nameET.getText().toString().trim();
            String vendorName=vendorNameET.getText().toString().trim();
            String vendorPhNo=vendorPhNoET.getText().toString().trim();

            if(name.equals("")||countET.getText().toString().trim().equals("")||vendorName.equals("")||vendorPhNo.equals("")){
                Toast.makeText(this,"Please enter valid details!",Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
            int count=Integer.parseInt(countET.getText().toString().trim());
            Items items=new Items();
            items.name=name;
            items.count=count;
            items.vendorName=vendorName;
            items.phoneNo=vendorPhNo;
            items.threshold=10;
            if(uid!=null)items.uid=Integer.parseInt(uid);
            AppDatabase.getDbInstance(this).userDao().insetItems(items);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}